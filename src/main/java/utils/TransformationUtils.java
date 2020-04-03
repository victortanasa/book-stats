package utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransformationUtils {

    private static final String COULD_NOT_EXTRACT_DATE_MESSAGE = "Could not extract date from %s. Exception was: %s";

    public static String getNullIfBlank(final String value) {
        return StringUtils.isBlank(value) ? null : value;
    }

    public static Integer getInteger(final String value) {
        return !StringUtils.isBlank(value) ? Integer.parseInt(value) : null;
    }

    public static Double getDouble(final String value) {
        return !StringUtils.isBlank(value) ? Double.parseDouble(value) : null;
    }

    public static LocalDate getDate(final DateTimeFormatter formatter, final String value) {
        try {
            return LocalDate.parse(value, formatter);
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_EXTRACT_DATE_MESSAGE, value, e.getMessage()));
            return null;
        }
    }

    public static String getCamelCase(final String value) {
        return StringUtils.removeAll(WordUtils.capitalizeFully(value), StringUtils.SPACE);
    }
}
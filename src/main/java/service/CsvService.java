package service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.util.stream.Collectors.toList;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import model.Statistic;
import utils.PrinterUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvService {

    private static final String CANNOT_COMBINE_DIFFERENT_KEYS_MESSAGE = "Cannot combine statistics with different keys! First key: %s | Second key: %s";
    private static final String COULD_NOT_LOAD_STATISTICS_MESSAGE = "Could not load statistics from file, cannot compute. Exception was: %s";
    private static final String COULD_NOT_WRITE_STATISTIC_MESSAGE = "Could not write statistic to file %s. Exception was: %s";

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##");

    private static final String RESOURCES_LOCATION = "src/main/resources/";
    private static final String CSV_LOCATION = RESOURCES_LOCATION + "csv/";

    private static final String CSV_FILE_NAME_FORMAT = "%s.csv";
    private static final String STATISTIC_FORMAT = "%s,%s";

    private static final String COMMA = ",";

    public void singleAxisStatisticToCsv(final Statistic statistic, final Map<String, ?> map) {
        final List<String[]> csvLines = getCsvLines(map);

        addLabels(csvLines, statistic.getKeyLabel(), statistic.getValueLabel());

        writeStatistic(statistic.getFileName(), csvLines);
    }

    public void dualAxisStatisticToCsv(final Statistic firstStatistic, final Statistic secondStatistic) {
        validateKeys(firstStatistic, secondStatistic);

        final Map<String, ?> firstMap = getStatisticFromFile(String.format(CSV_FILE_NAME_FORMAT, firstStatistic.getFileName()));
        final Map<String, ?> secondMap = getStatisticFromFile(String.format(CSV_FILE_NAME_FORMAT, secondStatistic.getFileName()));

        final Map<String, String> resultMap = newLinkedHashMap();

        Stream.of(secondMap, firstMap)
                .flatMap(m -> m.entrySet().stream())
                .forEach(entry -> mergeValues(resultMap, entry));

        final List<String[]> csv = getCsvLines(resultMap);

        writeStatistic(firstStatistic.getFileName() + secondStatistic.getFileName(), csv);
    }

    private static void addLabels(final List<String[]> csv, final String... labels) {
        csv.add(0, labels);
    }

    private static void mergeValues(final Map<String, String> resultMap, final Map.Entry<String, ?> entry) {
        final Object value = resultMap.get(entry.getKey());
        if (Objects.isNull(value)) {
            resultMap.put(entry.getKey(), entry.getValue().toString());
        } else {
            resultMap.put(entry.getKey(), String.format(STATISTIC_FORMAT, value, entry.getValue()));
        }
    }

    private static List<String[]> getCsvLines(final Map<String, ?> map) {
        return map.entrySet().stream()
                .map(CsvService::toCsvLine)
                .collect(toList());
    }

    private static String[] toCsvLine(final Map.Entry<String, ?> entry) {
        final ArrayList<String> list = newArrayList(entry.getKey());

        if (entry.getValue().toString().contains(COMMA)) {
            list.addAll(Arrays.stream(entry.getValue().toString().split(COMMA))
                    .map(value -> formatDoubleIfNecessary(value).toString())
                    .collect(toList()));
        } else {
            list.add(formatDoubleIfNecessary(entry.getValue()).toString());
        }

        return list.toArray(new String[0]);
    }

    private static Map<String, ?> getStatisticFromFile(final String fileName) {
        try {
            return getCsvLines(fileName).stream()
                    .collect(Collectors.toMap(record -> record[0], record -> record[1],
                            (firstEntry, secondEntry) -> firstEntry,
                            LinkedHashMap::new));
        } catch (final Exception e) {
            throw new IllegalStateException(String.format(COULD_NOT_LOAD_STATISTICS_MESSAGE, e));
        }
    }

    private static List<String[]> getCsvLines(final String fileName) throws IOException {
        return new CSVReader(new FileReader(Paths.get(CSV_LOCATION + fileName).toFile())).readAll();
    }

    private void writeStatistic(final String fileName, final List<String[]> csvLines) {
        try {
            final Path path = Paths.get(CSV_LOCATION + String.format(CSV_FILE_NAME_FORMAT, fileName));
            final CSVWriter csvWriter = new CSVWriter(new FileWriter(path.toString()));
            csvWriter.writeAll(csvLines);
            csvWriter.close();
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_WRITE_STATISTIC_MESSAGE, fileName, e));
        }
    }

    private void validateKeys(final Statistic firstStatistic, final Statistic secondStatistic) {
        if (!firstStatistic.getKeyLabel().equals(secondStatistic.getKeyLabel())) {
            throw new IllegalArgumentException(String.format(CANNOT_COMBINE_DIFFERENT_KEYS_MESSAGE,
                    firstStatistic.getKeyLabel(), secondStatistic.getKeyLabel()));
        }
    }

    private static Object formatDoubleIfNecessary(final Object value) {
        return value instanceof Double ? Double.parseDouble(DECIMAL_FORMAT.format(value)) : value;
    }

}
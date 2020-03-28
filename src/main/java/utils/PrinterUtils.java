package utils;

import static com.google.common.collect.Maps.newHashMap;

import model.Book;
import model.enums.BookField;
import model.enums.Shelve;
import model.enums.StatisticName;
import model.enums.sort.SortBy;
import model.enums.sort.SortOrder;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrinterUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###.##");
    private static final String MISSING_DATA = "Books with missing data in shelf [%s]: %s";
    private static final String MISSING_FIELDS = "Missing Fields: ";

    private static final String SEPARATOR_LARGE = StringUtils.repeat("-", 60);
    private static final String SEPARATOR_SMALL = StringUtils.repeat("-", 30);
    private static final String STATISTIC_PRINT_FORMAT = "%s: %s";
    private static final String NEWLINE = "\n";

    private static final Map<SortBy, Comparator<Book>> SORT_FUNCTIONS;

    static {
        SORT_FUNCTIONS = newHashMap();
        SORT_FUNCTIONS.put(SortBy.DATE_FINISHED, Comparator.comparing(Book::getDateFinished));
        SORT_FUNCTIONS.put(SortBy.PUBLICATION_YEAR, Comparator.comparing(Book::getPublicationYear));
        SORT_FUNCTIONS.put(SortBy.PAGE_NUMBER, Comparator.comparing(Book::getPageNumber));
        SORT_FUNCTIONS.put(SortBy.AUTHOR, Comparator.comparing(Book::getAuthorAsString));
        SORT_FUNCTIONS.put(SortBy.RATING, Comparator.comparing(Book::getRating));
        SORT_FUNCTIONS.put(SortBy.TITLE, Comparator.comparing(Book::getTitle));
    }

    public static void printBooks(final List<Book> books, final SortBy sortBy, final SortOrder sortOrder) {
        final Comparator<Book> comparator = SortOrder.ASC.equals(sortOrder) ?
                SORT_FUNCTIONS.get(sortBy) : SORT_FUNCTIONS.get(sortBy).reversed();

        books.stream()
                .sorted(comparator)
                .forEach(book -> System.out.println(book + NEWLINE + SEPARATOR_LARGE));
    }

    public static void printMapStatistic(final StatisticName statisticName, final Map<String, ?> map) {
        System.out.println(statisticName.getStringValue() + NEWLINE);

        map.forEach((key, value) -> System.out.println(String.format(STATISTIC_PRINT_FORMAT, key, formatNumber(value))));

        System.out.println(SEPARATOR_LARGE);
    }

    public static void printListStatistic(final StatisticName statisticName, final List<String> list) {
        System.out.println(statisticName.getStringValue() + NEWLINE);

        list.forEach(element -> System.out.println(element + NEWLINE + SEPARATOR_SMALL));

        System.out.println(SEPARATOR_LARGE);
    }

    public static void printSingleValueStatistic(final StatisticName statisticName, final Number value) {
        System.out.println(statisticName.getStringValue() + NEWLINE);

        System.out.println(formatNumber(value));

        System.out.println(SEPARATOR_LARGE);
    }

    public static void printMissingData(final Shelve shelve, final Map<Book, List<BookField>> bookMissingFieldMap) {
        final Map<Book, List<BookField>> mapWithValues = bookMissingFieldMap.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(String.format(MISSING_DATA, shelve, mapWithValues.size()) + NEWLINE);

        mapWithValues.forEach((book, missingFields) -> System.out.println(book.toStringShort()
                + NEWLINE
                + MISSING_FIELDS
                + missingFields
                + NEWLINE
                + SEPARATOR_SMALL));

        System.out.println(SEPARATOR_LARGE);
    }

    public static void printSimple(final String string) {
        System.out.println(string);
    }

    private static Object formatNumber(final Object value) {
        return DECIMAL_FORMAT.format(value);
    }

}
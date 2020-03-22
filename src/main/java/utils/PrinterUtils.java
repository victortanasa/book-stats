package utils;

import static com.google.common.collect.Maps.newHashMap;

import model.Book;
import model.SortBy;
import model.SortOrder;
import model.Statistic;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class PrinterUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private static final String SEPARATOR = "-------------------------------";
    private static final String STATISTIC_PRINT_FORMAT = "%s: %s";
    private static final String NEWLINE = "\n";

    private static final Map<SortBy, Comparator<Book>> SORT_FUNCTIONS;

    static {
        SORT_FUNCTIONS = newHashMap();
        SORT_FUNCTIONS.put(SortBy.DATE_FINISHED, Comparator.comparing(Book::getDateFinished));
        SORT_FUNCTIONS.put(SortBy.RELEASE_YEAR, Comparator.comparing(Book::getReleaseYear));
        SORT_FUNCTIONS.put(SortBy.PAGE_NUMBER, Comparator.comparing(Book::getPageNumber));
        SORT_FUNCTIONS.put(SortBy.AUTHOR, Comparator.comparing(Book::getAuthor));
        SORT_FUNCTIONS.put(SortBy.RATING, Comparator.comparing(Book::getRating));
        SORT_FUNCTIONS.put(SortBy.TITLE, Comparator.comparing(Book::getTitle));
        SORT_FUNCTIONS.put(SortBy.GENRE, Comparator.comparing(Book::getGenre));
    }

    public static void printBooks(final Set<Book> books, final SortBy sortBy, final SortOrder sortOrder) {
        final Comparator<Book> comparator = SortOrder.ASC.equals(sortOrder) ?
                SORT_FUNCTIONS.get(sortBy) : SORT_FUNCTIONS.get(sortBy).reversed();

        books.stream().
                sorted(comparator)
                .forEach(book -> System.out.println(book + NEWLINE + SEPARATOR));
    }

    public static void printStatistic(final Statistic statistic, final Map<String, ?> map) {
        System.out.println(statistic.getStringValue() + NEWLINE);

        map.forEach((key, value) -> System.out.println(String.format(STATISTIC_PRINT_FORMAT, key, formatDoubleIfNecessary(value))));

        System.out.println(SEPARATOR);
    }

    static void simplePrint(final String string) {
        System.out.println(string);
    }

    private static Object formatDoubleIfNecessary(final Object value) {
        return value instanceof Double ? DECIMAL_FORMAT.format(value) : value;
    }

}
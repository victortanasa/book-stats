package utils;

import model.Book;
import model.Statistic;

import java.util.Map;
import java.util.Set;

public class PrinterUtils {

    private static final String SEPARATOR = "-------------------------------";
    private static final String NEWLINE = "\n";

    public static void simplePrint(final String string) {
        System.out.println(string);
    }

    //TODO: print asc / desc by condition
    public static void printBooks(final Set<Book> books) {
        books.forEach(book -> {
            System.out.println(book);
            System.out.println(SEPARATOR);
        });
    }

    public static void printStatistic(final Statistic statistic, final Map<String, Long> map) {
        System.out.println(statistic.getStringValue() + NEWLINE);

        map.entrySet().forEach(System.out::println);

        System.out.println(SEPARATOR);
    }
}
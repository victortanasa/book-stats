package service;

import static com.google.common.collect.Maps.newHashMap;
import static model.Statistic.*;

import model.Book;
import model.Statistic;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

//TODO: average rating / page number / for author
//TODO: most read authors per year
//TODO: pages per month
//TODO: authors with most favourites

public class StatisticsService {

    private static final String DECADE_FORMAT = "%d's";

    private static final Map<Statistic, BookStatisticFunctions> STATISTIC_COLLECTOR_MAP;

    static {
        STATISTIC_COLLECTOR_MAP = newHashMap();
        STATISTIC_COLLECTOR_MAP.put(MOST_READ_AUTHORS_BY_BOOKS_COUNT, new BookStatisticFunctions(Book::getAuthor, Collectors.counting()));
        STATISTIC_COLLECTOR_MAP.put(MOST_READ_AUTHORS_BY_PAGE_COUNT, new BookStatisticFunctions(Book::getAuthor, Collectors.summingLong(Book::getPageNumber)));
        STATISTIC_COLLECTOR_MAP.put(MOST_READ_GENRES, new BookStatisticFunctions(book -> book.getGenre().getStringValue(), Collectors.counting()));
        STATISTIC_COLLECTOR_MAP.put(BOOKS_BY_DECADE, new BookStatisticFunctions(book -> getDecade(book.getReleaseYear()), Collectors.counting()));
        STATISTIC_COLLECTOR_MAP.put(BOOKS_BY_RATING, new BookStatisticFunctions(book -> book.getRating().toString(), Collectors.counting()));
        STATISTIC_COLLECTOR_MAP.put(AVERAGE_RATING_FOR_AUTHORS, new BookStatisticFunctions(Book::getAuthor, Collectors.averagingInt(Book::getRating)));
//        STATISTIC_COLLECTOR_MAP.put(AUTHORS_WITH_MOST_FAVOURITES, new BookStatisticFunctions(Book::getAuthor, Collectors.summingInt(Book::isFavoriteInteger)));
    }

    //TODO: rewrite - one method for each statistic, place "function in map"

    private Set<Book> library;

    public StatisticsService(final Set<Book> library) {
        this.library = library;
    }

    public Map<String, ? extends Number> getStatistic(final Statistic statistic) {
        final BookStatisticFunctions functions = STATISTIC_COLLECTOR_MAP.get(statistic);

        return library.stream()
                .collect(Collectors.groupingBy(functions.getBookFunction(), functions.getBookCollector()));
    }

//    private static <T> Map<T, Long> sortDescendingByValue(final Map<T, Long> map) {
//        return map.entrySet()
//                .stream()
//                .sorted((Map.Entry.<T, Long>comparingByValue().reversed()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//    }

    private static String getDecade(final Integer releaseYear) {
        return String.format(DECADE_FORMAT, releaseYear % 100 - releaseYear % 10);
    }

    static class BookStatisticFunctions {

        private Function<Book, String> bookFunction;

        private Collector<Book, ?, ? extends Number> bookCollector;

        BookStatisticFunctions(final Function<Book, String> bookFunction, final Collector<Book, ?, ? extends Number> bookCollector) {
            this.bookCollector = bookCollector;
            this.bookFunction = bookFunction;
        }

        Function<Book, String> getBookFunction() {
            return bookFunction;
        }

        Collector<Book, ?, ? extends Number> getBookCollector() {
            return bookCollector;
        }
    }

}
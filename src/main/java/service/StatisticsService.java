package service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static model.Statistic.*;

import model.Book;
import model.Statistic;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StatisticsService {

    private static final String DECADE_FORMAT = "%d's";

    private static final Map<Statistic, Collector<Book, ?, Map<String, Long>>> LONG_STATISTIC_COLLECTOR_MAP;
    private static final Map<Statistic, Collector<Book, ?, Map<String, Double>>> DOUBLE_STATISTIC_COLLECTOR_MAP;

    private static final Map<Statistic, Predicate<Book>> FILTER_MAP;

    private static final List<Statistic> AVERAGE_STATISTICS;

    //TODO: filter map -> default none

    static {
        LONG_STATISTIC_COLLECTOR_MAP = newHashMap();
        LONG_STATISTIC_COLLECTOR_MAP.put(MOST_READ_AUTHORS_BY_PAGE_COUNT, Collectors.groupingBy(Book::getAuthor, Collectors.summingLong(Book::getPageNumber)));
        LONG_STATISTIC_COLLECTOR_MAP.put(AUTHORS_WITH_MOST_FAVOURITES, Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
        LONG_STATISTIC_COLLECTOR_MAP.put(MOST_READ_AUTHORS_BY_BOOK_COUNT, Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
        LONG_STATISTIC_COLLECTOR_MAP.put(MOST_READ_GENRES, Collectors.groupingBy(book -> book.getGenre().getStringValue(), Collectors.counting()));
        LONG_STATISTIC_COLLECTOR_MAP.put(BOOKS_BY_DECADE, Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
        LONG_STATISTIC_COLLECTOR_MAP.put(BOOKS_BY_RATING, Collectors.groupingBy(book -> book.getRating().toString(), Collectors.counting()));

        DOUBLE_STATISTIC_COLLECTOR_MAP = newHashMap();
        DOUBLE_STATISTIC_COLLECTOR_MAP.put(AVERAGE_PAGE_NUMBER_FOR_AUTHORS, Collectors.groupingBy(Book::getAuthor, Collectors.averagingInt(Book::getPageNumber)));
        DOUBLE_STATISTIC_COLLECTOR_MAP.put(AVERAGE_RATING_FOR_AUTHORS, Collectors.groupingBy(Book::getAuthor, Collectors.averagingInt(Book::getRating)));

        FILTER_MAP = newHashMap();
        FILTER_MAP.put(AUTHORS_WITH_MOST_FAVOURITES, Book::isFavorite);

        AVERAGE_STATISTICS = newArrayList(AVERAGE_PAGE_NUMBER_FOR_AUTHORS, AVERAGE_RATING_FOR_AUTHORS);
    }

    private Set<Book> library;

    //TODO: pages per month
    //TODO: most read authors per year - books and page number
    public StatisticsService(final Set<Book> library) {
        this.library = library;
    }

    public <K, V> Map<K, V> getStatistic(final Statistic statistic) {
        final Collector<Book, ?, ? extends Map<String, ?>> collector = AVERAGE_STATISTICS.contains(statistic) ?
                DOUBLE_STATISTIC_COLLECTOR_MAP.get(statistic) : LONG_STATISTIC_COLLECTOR_MAP.get(statistic);

        final Predicate<Book> filter = Objects.isNull(FILTER_MAP.get(statistic)) ? always -> true : FILTER_MAP.get(statistic);

        return sortDescendingByValue(library.stream()
                .filter(filter)
                .collect(collector));
    }

//    private static <T, V extends Comparable<V>> Map<T, V> sortDescendingByValue(final Map<T, V> map) {
//        return map.entrySet()
//                .stream()
//                .sorted(Collections.reverseOrder(comparingByValue()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//    }

    private static Map sortDescendingByValue(final Map map) {
        return map;
    }

    private static String getDecade(final Integer releaseYear) {
        return String.format(DECADE_FORMAT, releaseYear - releaseYear % 10);
    }

}
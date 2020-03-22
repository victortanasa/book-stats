package service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static model.Statistic.*;

import model.Book;
import model.Statistic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StatisticsService {

    private static final DateTimeFormatter MONTH_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private static final String DECADE_FORMAT = "%d's";

    private static final Map<Statistic, Collector<Book, ?, Map<String, Long>>> LONG_STATISTIC_COLLECTOR_MAP;
    private static final Map<Statistic, Collector<Book, ?, Map<String, Double>>> DOUBLE_STATISTIC_COLLECTOR_MAP;

    private static final Map<Statistic, Predicate<Book>> FILTER_MAP;

    private static final List<Statistic> AVERAGE_STATISTICS;

    static {
        LONG_STATISTIC_COLLECTOR_MAP = newHashMap();
        LONG_STATISTIC_COLLECTOR_MAP.put(MOST_READ_AUTHORS_BY_PAGE_COUNT, Collectors.groupingBy(Book::getAuthor, Collectors.summingLong(Book::getPageNumber)));
        LONG_STATISTIC_COLLECTOR_MAP.put(MOST_READ_GENRES, Collectors.groupingBy(book -> book.getGenre().getStringValue(), Collectors.counting()));
        LONG_STATISTIC_COLLECTOR_MAP.put(BOOKS_BY_DECADE, Collectors.groupingBy(book -> getDecade(book.getReleaseYear()), Collectors.counting()));
        LONG_STATISTIC_COLLECTOR_MAP.put(BOOKS_BY_RATING, Collectors.groupingBy(book -> book.getRating().toString(), Collectors.counting()));
        LONG_STATISTIC_COLLECTOR_MAP.put(MOST_READ_AUTHORS_BY_BOOK_COUNT, Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
        LONG_STATISTIC_COLLECTOR_MAP.put(AUTHORS_WITH_MOST_FAVOURITES, Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
        LONG_STATISTIC_COLLECTOR_MAP.put(BOOKS_PER_MONTH, Collectors.groupingBy(book -> getMonth(book.getDateFinished()), Collectors.counting()));
        LONG_STATISTIC_COLLECTOR_MAP.put(PAGES_PER_MONTH, Collectors.groupingBy(book -> getMonth(book.getDateFinished()), Collectors.summingLong(Book::getPageNumber)));

        DOUBLE_STATISTIC_COLLECTOR_MAP = newHashMap();
        DOUBLE_STATISTIC_COLLECTOR_MAP.put(AVERAGE_PAGE_NUMBER_FOR_AUTHORS, Collectors.groupingBy(Book::getAuthor, Collectors.averagingInt(Book::getPageNumber)));
        DOUBLE_STATISTIC_COLLECTOR_MAP.put(AVERAGE_RATING_FOR_AUTHORS, Collectors.groupingBy(Book::getAuthor, Collectors.averagingInt(Book::getRating)));

        FILTER_MAP = newHashMap();
        FILTER_MAP.put(AUTHORS_WITH_MOST_FAVOURITES, Book::isFavorite);

        AVERAGE_STATISTICS = newArrayList(AVERAGE_PAGE_NUMBER_FOR_AUTHORS, AVERAGE_RATING_FOR_AUTHORS);
    }

    private Set<Book> library;

    //TODO: most read authors per year - books and page number - sort by what
    public StatisticsService(final Set<Book> library) {
        this.library = library;
    }

    public Map<String, ?> getStatistic(final Statistic statistic) {
        final Collector<Book, ?, ? extends Map<String, ?>> collector = AVERAGE_STATISTICS.contains(statistic) ?
                DOUBLE_STATISTIC_COLLECTOR_MAP.get(statistic) : LONG_STATISTIC_COLLECTOR_MAP.get(statistic);

        final Predicate<Book> filter = Objects.isNull(FILTER_MAP.get(statistic)) ? always -> true : FILTER_MAP.get(statistic);

        return sortDescendingByValue(library.stream()
                .filter(filter)
                .collect(collector));
    }

    private Map<String, ?> sortDescendingByValue(final Map<String, ?> map) {
        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(getDescendingEntryComparator()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private static Comparator<Map.Entry<String, ?>> getDescendingEntryComparator() {
        return (o1, o2) -> {
            final Double firstDouble = new Double(o1.getValue().toString());
            final Double secondDouble = new Double(o2.getValue().toString());

            return firstDouble.equals(secondDouble) ? 0 : firstDouble > secondDouble ? 1 : -1;

        };
    }

    private static String getDecade(final Integer releaseYear) {
        return String.format(DECADE_FORMAT, releaseYear - releaseYear % 10);
    }

    private static String getMonth(final LocalDate dateRead) {
        return MONTH_ONLY_FORMATTER.format(dateRead.withDayOfMonth(1));
    }

}
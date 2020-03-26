package service;

import static com.google.common.collect.Maps.newHashMap;
import static model.Statistic.*;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import model.GoodReadsBook;
import model.Statistic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsService {

    private static final DateTimeFormatter MONTH_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private static final String DECADE_FORMAT = "%d's";

    private Map<Statistic, Function<Integer, List<String>>> listStatistics = newHashMap();
    private Map<Statistic, Supplier<Map<String, ?>>> mapStatistic = newHashMap();
    private Map<Statistic, Supplier<Double>> singleValueStatistics = newHashMap();

    private List<GoodReadsBook> library;

    public StatisticsService(final List<GoodReadsBook> library) {
        this.library = library;

        mapStatistic.put(MOST_BOOKS_READ_BY_PUBLISHED_DECADE, this::getMostBooksReadByPublishedDecade);
        mapStatistic.put(BOOKS_READ_PER_MONTH, this::getBooksReadPerMonth);
        mapStatistic.put(PAGES_READ_PER_MONTH, this::getPagesReadPerMonth);
    }

    public Map<String, ?> getMapStatistic(final Statistic statistic) {
        return sortDescendingByValue(mapStatistic.get(statistic).get());
    }

    private Map<String, ?> getMostBooksReadByPublishedDecade() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getDecade(book.getPublicationYear()), Collectors.counting()));
    }

    private Map<String, ?> getBooksReadPerMonth() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getMonth(book.getDateFinished()), Collectors.counting()));
    }

    private Map<String, ?> getPagesReadPerMonth() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getMonth(book.getDateFinished()), Collectors.summingLong(GoodReadsBook::getPageNumber)));
    }

    private static Map<String, ?> sortDescendingByValue(final Map<String, ?> map) {
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
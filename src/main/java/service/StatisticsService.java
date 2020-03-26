package service;

import static com.google.common.collect.Maps.newHashMap;
import static model.Statistic.*;

import com.google.common.base.Supplier;
import model.Book;
import model.Statistic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsService {

    private static final DateTimeFormatter MONTH_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private static final String DECADE_FORMAT = "%d's";

    private Map<Statistic, Supplier<Double>> singleValueStatistics;
    private Map<Statistic, Supplier<Map<String, ?>>> mapStatistic;

    private List<Book> library;

    public StatisticsService(final List<Book> library) {
        this.library = library;

        mapStatistic = newHashMap();
        mapStatistic.put(MOST_READ_AUTHORS_BY_PAGE_COUNT, this::getMostReadAuthorsByPageCount);
        mapStatistic.put(MOST_READ_AUTHORS_BY_BOOK_COUNT, this::getMostReadAuthorsBookCount);

        mapStatistic.put(BOOKS_READ_PER_MONTH, this::getBooksReadPerMonth);
        mapStatistic.put(PAGES_READ_PER_MONTH, this::getPagesReadPerMonth);

        mapStatistic.put(AVERAGE_RATING_FOR_AUTHORS, this::getAverageRatingsForAuthors);
        mapStatistic.put(AVERAGE_PAGE_NUMBER_FOR_AUTHORS, this::getAveragePageCountForAuthors);
        mapStatistic.put(AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR, this::getAverageDaysToReadABookPerAuthor);

        mapStatistic.put(MOST_BOOKS_READ_BY_PUBLISHED_DECADE, this::getMostBooksReadByPublishedDecade);
        mapStatistic.put(MOST_POPULAR_AUTHORS_BY_AVERAGE_NUMBER_OF_RATINGS, this::getMostPopularAuthorsByAverageNumberOfRatings);

        mapStatistic.put(RATINGS_DISTRIBUTION, this::getRatingsDistribution);

        singleValueStatistics = newHashMap();
        singleValueStatistics.put(AVERAGE_RATING, this::getAverageRating);
        singleValueStatistics.put(AVERAGE_DAYS_TO_READ_A_BOOK, this::getAverageDaysToReadABook);
        singleValueStatistics.put(AVERAGE_PAGES_READ_PER_MONTH, this::getAveragePagesReadPerMonth);
        singleValueStatistics.put(AVERAGE_BOOKS_READ_PER_MONTH, this::getAverageBooksReadPerMonth);
    }

    public Map<String, ?> getMapStatistic(final Statistic statistic) {
        return sortDescendingByValue(mapStatistic.get(statistic).get());
    }

    public Number getSingeValueStatistic(final Statistic statistic) {
        return singleValueStatistics.get(statistic).get();
    }

    private Map<String, ?> getMostReadAuthorsByPageCount() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.summingInt(Book::getPageNumber)));
    }

    private Map<String, ?> getMostReadAuthorsBookCount() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.counting()));
    }

    private Map<String, ?> getBooksReadPerMonth() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getMonth(book.getDateFinished()), Collectors.counting()));
    }

    private Map<String, ?> getPagesReadPerMonth() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getMonth(book.getDateFinished()), Collectors.summingInt(Book::getPageNumber)));
    }

    private Map<String, ?> getAverageRatingsForAuthors() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.averagingInt(Book::getRating)));
    }

    private Map<String, ?> getAveragePageCountForAuthors() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.averagingInt(Book::getPageNumber)));
    }

    private Map<String, ?> getAverageDaysToReadABookPerAuthor() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.averagingLong(Book::getDaysReadIn)));
    }

    private Map<String, ?> getMostBooksReadByPublishedDecade() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getDecade(book.getPublicationYear()), Collectors.counting()));
    }

    private Map<String, ?> getMostPopularAuthorsByAverageNumberOfRatings() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.averagingInt(Book::getRatingsCount)));
    }

    private Map<String, ?> getRatingsDistribution() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> book.getRating().toString(), Collectors.counting()));
    }

    private Double getAverageRating() {
        return library.stream()
                .mapToInt(Book::getRating)
                .average()
                .orElse(-1);
    }

    private Double getAverageDaysToReadABook() {
        return library.stream()
                .mapToLong(Book::getDaysReadIn)
                .average()
                .orElse(-1);
    }

    private Double getAveragePagesReadPerMonth() {
        return getAverageFromMapValues(getPagesReadPerMonth());
    }

    private Double getAverageBooksReadPerMonth() {
        return getAverageFromMapValues(getBooksReadPerMonth());
    }

    private static Double getAverageFromMapValues(final Map<String, ?> map) {
        return map.entrySet().stream()
                .mapToDouble(entry -> Double.valueOf(entry.getValue().toString()))
                .average()
                .orElse(-1);
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
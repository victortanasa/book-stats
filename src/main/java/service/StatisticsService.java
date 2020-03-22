package service;

import static com.google.common.collect.Maps.newHashMap;
import static model.Statistic.*;

import com.google.common.base.Function;
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

    private Set<Book> library;

    private Map<Statistic, Function<Integer, List<String>>> listStatistics = newHashMap();
    private Map<Statistic, Supplier<Map<String, ?>>> mapStatistic = newHashMap();
    private Map<Statistic, Supplier<Double>> singleValueStatistics = newHashMap();

    //TODO: most read authors per year - books and page number - sort by what
    public StatisticsService(final Set<Book> library) {
        this.library = library;

        mapStatistic.put(MOST_READ_AUTHORS_BY_PAGE_COUNT, this::getMostReadAuthorsByPageCount);
        mapStatistic.put(MOST_READ_AUTHORS_BY_BOOK_COUNT, this::getMostReadAuthorsBookCount);
        mapStatistic.put(MOST_BOOKS_READ_BY_GENRE, this::getMostBooksReadByGenre);
        mapStatistic.put(MOST_BOOKS_READ_BY_PUBLISHED_DECADE, this::getMostBooksReadByPublishedDecade);

        mapStatistic.put(TOTAL_RATINGS, this::getTotalRatings);
        mapStatistic.put(BOOKS_READ_PER_MONTH, this::getBooksReadPerMonth);
        mapStatistic.put(PAGES_READ_PER_MONTH, this::getPagesReadPerMonth);

        mapStatistic.put(AUTHORS_WITH_MOST_FAVOURITES, this::getAuthorsWithMostFavourites);

        mapStatistic.put(AVERAGE_PAGE_NUMBER_FOR_AUTHORS, this::getAveragePageCountForAuthors);
        mapStatistic.put(AVERAGE_RATING_FOR_AUTHORS, this::getAverageRatingsForAuthors);
        mapStatistic.put(AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR, this::getAverageDaysToReadABookPerAuthor);

        listStatistics.put(SHORTEST_BOOKS, this::getShortestBooks);
        listStatistics.put(LONGEST_BOOKS, this::getLongestBooks);

        singleValueStatistics.put(AVERAGE_DAYS_TO_READ_A_BOOK, this::getAverageDaysToReadABook);
    }

    public Map<String, ?> getMapStatistic(final Statistic statistic) {
        return sortDescendingByValue(mapStatistic.get(statistic).get());
    }

    public List<String> getListStatistic(final Statistic statistic, final Integer limit) {
        return listStatistics.get(statistic).apply(limit);
    }

    public Number getSingeValueStatistic(final Statistic statistic) {
        return singleValueStatistics.get(statistic).get();
    }

    private Map<String, ?> getMostReadAuthorsByPageCount() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.summingLong(Book::getPageNumber)));
    }

    private Map<String, ?> getMostReadAuthorsBookCount() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
    }

    private Map<String, ?> getMostBooksReadByGenre() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> book.getGenre().getStringValue(), Collectors.counting()));
    }

    private Map<String, ?> getAveragePageCountForAuthors() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.averagingInt(Book::getPageNumber)));
    }

    private Map<String, ?> getMostBooksReadByPublishedDecade() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getDecade(book.getReleaseYear()), Collectors.counting()));
    }

    private Map<String, ?> getTotalRatings() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> book.getRating().toString(), Collectors.counting()));
    }

    private Map<String, ?> getAverageRatingsForAuthors() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.averagingInt(Book::getRating)));
    }

    private Map<String, ?> getBooksReadPerMonth() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getMonth(book.getDateFinished()), Collectors.counting()));
    }

    private Map<String, ?> getPagesReadPerMonth() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getMonth(book.getDateFinished()), Collectors.summingLong(Book::getPageNumber)));
    }

    private Map<String, ?> getAuthorsWithMostFavourites() {
        return library.stream()
                .filter(Book::isFavorite)
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
    }

    private Map<String, ?> getAverageDaysToReadABookPerAuthor() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.averagingLong(Book::getDaysReadIn)));
    }

    private Double getAverageDaysToReadABook() {
        return library.stream()
                .mapToLong(Book::getDaysReadIn)
                .average()
                .orElse(-1);
    }

    private List<String> getShortestBooks(final int limit) {
        return library.stream().
                sorted(Comparator.comparing(Book::getPageNumber))
                .limit(limit)
                .map(Book::toStringShortFormat)
                .collect(Collectors.toList());
    }

    //TODO: extract something
    private List<String> getLongestBooks(final int limit) {
        return library.stream().
                sorted(Comparator.comparing(Book::getPageNumber).reversed())
                .limit(limit)
                .map(Book::toStringShortFormat)
                .collect(Collectors.toList());
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
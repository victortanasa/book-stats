package service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.util.stream.Collectors.toSet;
import static model.enums.sort.SortOrder.DESC;
import static service.Statistics.*;

import com.google.common.base.Supplier;
import model.Book;
import model.Statistic;
import model.enums.sort.SortBy;
import model.enums.sort.SortOrder;
import org.apache.commons.lang3.tuple.Pair;
import service.processing.ShelveAggregator;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("all")
public class StatisticsService {

    private static final DateTimeFormatter MONTH_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter YEAR_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy");

    private static final String DECADE_FORMAT = "%d's";
    private static final int AUTHOR_LIMIT = 20;

    private Map<Statistic, Supplier<Number>> singleValueStatistics;
    private Map<Statistic, Supplier<Map<String, ? extends Comparable<?>>>> mapStatistic;

    private AvailableStatisticsService availableStatisticsService;
    private ShelveAggregator shelveAggregator;
    private Set<String> topAuthors;
    private List<Book> library;

    //TODO: don't init authors if page number null; or get by book number
    //TODO: scatter plot - date read + release year + don't filter by author
    public StatisticsService(final List<Book> library) {
        this.library = library;
        this.shelveAggregator = new ShelveAggregator();
        this.availableStatisticsService = new AvailableStatisticsService();

        initStatisticsMap();

        this.topAuthors = getTopAuthors();
    }

    private void initStatisticsMap() {
        mapStatistic = newHashMap();
        mapStatistic.put(MOST_READ_AUTHORS_BY_PAGE_COUNT, this::getMostReadAuthorsByPageCount);
        mapStatistic.put(MOST_READ_AUTHORS_BY_BOOK_COUNT, this::getMostReadAuthorsBookCount);

        mapStatistic.put(BOOKS_READ_PER_MONTH, this::getBooksReadPerMonth);
        mapStatistic.put(PAGES_READ_PER_MONTH, this::getPagesReadPerMonth);
        mapStatistic.put(PAGES_READ_PER_MONTH_MEDIAN, this::getPagesReadPerMonthMedian);
        mapStatistic.put(BOOKS_READ_PER_YEAR, this::getBooksReadPerYear);
        mapStatistic.put(PAGES_READ_PER_YEAR, this::getPagesReadPerYear);

        mapStatistic.put(AVERAGE_RATING_FOR_AUTHORS, this::getAverageRatingsForAuthors);
        mapStatistic.put(AVERAGE_PAGE_NUMBER_FOR_AUTHORS, this::getAveragePageNumberForAuthors);
        mapStatistic.put(AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR, this::getAverageDaysToReadABookPerAuthor);
        mapStatistic.put(AVERAGE_BOOK_LENGTH_PER_YEAR, this::getAverageBookLengthPerYear);
        mapStatistic.put(AVERAGE_PAGES_READ_PER_DAY_PER_MONTH, this::getAveragePagesReadPerDayPerMonth);

        mapStatistic.put(MOST_BOOKS_READ_BY_PUBLISHED_DECADE, this::getMostBooksReadByPublishedDecade);
        mapStatistic.put(MOST_POPULAR_AUTHORS_BY_AVERAGE_NUMBER_OF_RATINGS, this::getMostPopularAuthorsByAverageNumberOfRatings);

        mapStatistic.put(RATINGS_DISTRIBUTION, this::getRatingsDistribution);
        mapStatistic.put(FORMATS_DISTRIBUTION, this::getFormatsDistribution);
        mapStatistic.put(MOST_POPULAR_SHELVES, this::getMostPopularShelves);

        mapStatistic.put(AUTHOR_TITLE_AND_PUBLICATION_YEAR, this::getAuthorTitleAndPublicationYear);
        mapStatistic.put(AUTHOR_TITLE_AND_DATE_FINISHED, this::getAuthorTitleAndDateFinished);

        singleValueStatistics = newHashMap();
        singleValueStatistics.put(AVERAGE_RATING, this::getAverageRating);
        singleValueStatistics.put(NUMBER_OF_AUTHORS_READ, this::getNumberOfAuthorsRead);
        singleValueStatistics.put(AVERAGE_DAYS_TO_READ_A_BOOK, this::getAverageDaysToReadABook);
        singleValueStatistics.put(AVERAGE_PAGES_READ_PER_MONTH, this::getAveragePagesReadPerMonth);
        singleValueStatistics.put(AVERAGE_BOOKS_READ_PER_MONTH, this::getAverageBooksReadPerMonth);
    }

    public Map<String, ? extends Comparable<?>> getMapStatistic(final Statistic statistic) {
        final Map<String, ? extends Comparable<?>> statistics = mapStatistic.get(statistic).get();

        return SortBy.KEY.equals(statistic.getSortBy()) ?
                sortMapByKey(statistics, statistic.getSortOrder(), statistic.getResultLimit()) :
                sortMapByValue(statistics, statistic.getSortOrder(), statistic.getResultLimit());
    }

    public Number getSingeValueStatistic(final Statistic statistic) {
        return singleValueStatistics.get(statistic).get();
    }

    public Set<String> getTopAuthors() {
        final List<String> topAuthorsByPageCount = pageCountIsAvailable() ?
                getKeys(getMapStatistic(MOST_READ_AUTHORS_BY_PAGE_COUNT)) :
                newArrayList();

        final List<String> topAuthorsByBookCount = getKeys(getMapStatistic(MOST_READ_AUTHORS_BY_BOOK_COUNT));

        return Stream.of(topAuthorsByBookCount, topAuthorsByPageCount)
                .flatMap(Collection::stream)
                .limit(AUTHOR_LIMIT)
                .collect(toSet());
    }

    private boolean pageCountIsAvailable() {
        return availableStatisticsService.getAvailableStatistics(library).contains(MOST_READ_AUTHORS_BY_PAGE_COUNT);
    }

    private Map<String, ? extends Comparable<?>> getMostReadAuthorsByPageCount() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.summingInt(Book::getPageNumber)));
    }

    private Map<String, ? extends Comparable<?>> getMostReadAuthorsBookCount() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.counting()));
    }

    private Map<String, ? extends Comparable<?>> getBooksReadPerMonth() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getMonth(book.getDateFinished()), Collectors.counting()));
    }

    private Map<String, ? extends Comparable<?>> getPagesReadPerMonth() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getMonth(book.getDateFinished()), Collectors.summingInt(Book::getPageNumber)));
    }

    private Map<String, ? extends Comparable<?>> getPagesReadPerMonthMedian() {
        return library.stream()
                .map(this::getPagesReadPerMonths)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.summingDouble(Pair::getValue)));
    }

    private Map<String, ? extends Comparable<?>> getBooksReadPerYear() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getYear(book.getDateFinished()), Collectors.counting()));
    }

    private Map<String, ? extends Comparable<?>> getPagesReadPerYear() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getYear(book.getDateFinished()), Collectors.summingInt(Book::getPageNumber)));
    }

    private Map<String, ? extends Comparable<?>> getAverageRatingsForAuthors() {
        return library.stream()
                .filter(book -> topAuthors.contains(book.getAuthorAsString()))
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.averagingInt(Book::getRating)));
    }

    private Map<String, ? extends Comparable<?>> getAveragePageNumberForAuthors() {
        return library.stream()
                .filter(book -> topAuthors.contains(book.getAuthorAsString()))
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.averagingInt(Book::getPageNumber)));
    }

    private Map<String, ? extends Comparable<?>> getAverageDaysToReadABookPerAuthor() {
        return library.stream()
                .filter(book -> topAuthors.contains(book.getAuthorAsString()))
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.averagingLong(Book::getDaysReadIn)));
    }

    private Map<String, ? extends Comparable<?>> getAverageBookLengthPerYear() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getYear(book.getDateFinished()), Collectors.averagingInt(Book::getPageNumber)));
    }

    private Map<String, ? extends Comparable<?>> getAveragePagesReadPerDayPerMonth() {
        return getPagesReadPerMonthMedian().entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), new Double(entry.getValue().toString()) / getNumberOfDaysInAMonth(entry.getKey())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private Map<String, ? extends Comparable<?>> getMostBooksReadByPublishedDecade() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getDecade(book.getPublicationYear()), Collectors.counting()));
    }

    private Map<String, ? extends Comparable<?>> getMostPopularAuthorsByAverageNumberOfRatings() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthorAsString, Collectors.averagingInt(Book::getRatingsCount)));
    }

    private Map<String, ? extends Comparable<?>> getRatingsDistribution() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> book.getRating().toString(), Collectors.counting()));
    }

    private Map<String, ? extends Comparable<?>> getFormatsDistribution() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getFormat, Collectors.counting()));
    }

    private Map<String, ? extends Comparable<?>> getAuthorTitleAndPublicationYear() {
        return library.stream()
                .collect(Collectors.toMap(Book::getAuthorAndTitle, Book::getPublicationYear));
    }

    private Map<String, ? extends Comparable<?>> getAuthorTitleAndDateFinished() {
        return library.stream()
                .collect(Collectors.toMap(Book::getAuthorAndTitle, Book::getDateFinished));
    }

    private Map<String, ? extends Comparable<?>> getMostPopularShelves() {
        final List<String> mostPopularShelves = library.stream()
                .map(Book::getShelves)
                .map(shelves -> shelveAggregator.getTopShelves(shelves))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return mostPopularShelves.stream()
                .collect(Collectors.groupingBy(shelve -> shelve, Collectors.counting()));
    }

    private Double getAverageRating() {
        return library.stream()
                .mapToInt(Book::getRating)
                .average()
                .orElse(-1);
    }

    private Integer getNumberOfAuthorsRead() {
        return library.stream()
                .map(Book::getAuthorAsString)
                .collect(toSet())
                .size();
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

    private List<Pair<String, Double>> getPagesReadPerMonths(final Book book) {
        return !book.getDateStarted().getMonth().equals(book.getDateFinished().getMonth()) ?
                getPagesReadForDifferentMonths(book) : getPagesReadForOneMonth(book);
    }

    private ArrayList<Pair<String, Double>> getPagesReadForOneMonth(final Book book) {
        return newArrayList(Pair.of(getMonth(book.getDateFinished()), new Double(book.getPageNumber())));
    }

    private List<Pair<String, Double>> getPagesReadForDifferentMonths(final Book book) {
        final long daysUntilLastOfMonth = DAYS.between(book.getDateStarted(), book.getDateStarted().with(lastDayOfMonth())) + 1;
        final long daysSinceFirstOfMonth = DAYS.between(book.getDateFinished().with(firstDayOfMonth()), book.getDateFinished()) + 1;

        final double pagesPerDay = ((double) book.getPageNumber() / (daysUntilLastOfMonth + daysSinceFirstOfMonth));

        final double pageCountForMonthStarted = pagesPerDay * daysUntilLastOfMonth;
        final double pageCountForMonthFinished = pagesPerDay * daysSinceFirstOfMonth;

        return newArrayList(
                Pair.of(getMonth(book.getDateStarted()), pageCountForMonthStarted),
                Pair.of(getMonth(book.getDateFinished()), pageCountForMonthFinished));
    }

    private List<String> getKeys(final Map<String, ?> map) {
        return map.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <K extends Comparable, V> Map<K, V> sortMapByKey(final Map<K, V> map, final SortOrder sortOrder, final Integer limit) {
        final Comparator<Map.Entry<K, V>> comparator = DESC.equals(sortOrder) ?
                Map.Entry.<K, V>comparingByKey().reversed() : Map.Entry.comparingByKey();

        return map.entrySet().stream()
                .sorted(comparator)
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @SuppressWarnings("unchecked")
    private static <K, V extends Comparable> Map<K, V> sortMapByValue(final Map<K, V> map, final SortOrder sortOrder, final Integer limit) {
        final Comparator<Map.Entry<K, V>> comparator = DESC.equals(sortOrder) ?
                Map.Entry.<K, V>comparingByValue().reversed() : Map.Entry.comparingByValue();

        return map.entrySet().stream()
                .sorted(comparator)
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private static String getDecade(final Integer releaseYear) {
        return String.format(DECADE_FORMAT, releaseYear - releaseYear % 10);
    }

    private static String getMonth(final LocalDate dateRead) {
        return MONTH_ONLY_FORMATTER.format(dateRead.withDayOfMonth(1));
    }

    private static String getYear(final LocalDate dateRead) {
        return YEAR_ONLY_FORMATTER.format(dateRead);
    }

    private static Integer getNumberOfDaysInAMonth(final String month) {
        return YearMonth.parse(month, MONTH_ONLY_FORMATTER).atDay(1).lengthOfMonth();
    }

}
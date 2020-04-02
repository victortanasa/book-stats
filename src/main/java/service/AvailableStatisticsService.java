package service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;
import static service.Statistics.*;

import model.Book;
import model.Statistic;
import model.enums.BookField;
import service.processing.BookFieldValidator;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AvailableStatisticsService {

    private static final BookFieldValidator BOOK_FIELD_VALIDATOR = new BookFieldValidator();

    private static final Map<BookField, List<Statistic>> REQUIRED_FIELDS_FOR_STATISTICS;
    private static final List<Statistic> ALWAYS_AVAILABLE_STATISTIC_NAMES;

    //TODO: unavailable stats?
    static {
        ALWAYS_AVAILABLE_STATISTIC_NAMES = newArrayList();
        ALWAYS_AVAILABLE_STATISTIC_NAMES.add(MOST_READ_AUTHORS_BY_BOOK_COUNT);
        ALWAYS_AVAILABLE_STATISTIC_NAMES.add(MOST_POPULAR_SHELVES);
        ALWAYS_AVAILABLE_STATISTIC_NAMES.add(NUMBER_OF_AUTHORS_READ);

        REQUIRED_FIELDS_FOR_STATISTICS = newHashMap();
        REQUIRED_FIELDS_FOR_STATISTICS.put(BookField.FORMAT, newArrayList(FORMATS_DISTRIBUTION));
        REQUIRED_FIELDS_FOR_STATISTICS.put(BookField.PAGE_NUMBER, newArrayList(
                PAGES_READ_PER_MONTH,
                PAGES_READ_PER_MONTH_MEDIAN,
                PAGES_READ_PER_YEAR,
                MOST_READ_AUTHORS_BY_PAGE_COUNT,
                AVERAGE_PAGE_NUMBER_FOR_AUTHORS,
                AVERAGE_PAGES_READ_PER_MONTH,
                AVERAGE_BOOK_LENGTH_PER_YEAR,
                AVERAGE_PAGES_READ_PER_DAY_PER_MONTH));
        REQUIRED_FIELDS_FOR_STATISTICS.put(BookField.DATE_FINISHED, newArrayList(
                AVERAGE_DAYS_TO_READ_A_BOOK,
                AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR,
                AVERAGE_BOOKS_READ_PER_MONTH,
                BOOKS_READ_PER_MONTH,
                BOOKS_READ_PER_YEAR,
                AVERAGE_BOOK_LENGTH_PER_YEAR,
                AVERAGE_PAGES_READ_PER_DAY_PER_MONTH,
                AUTHOR_TITLE_AND_DATE_FINISHED));
        REQUIRED_FIELDS_FOR_STATISTICS.put(BookField.DATE_STARTED, newArrayList(
                AVERAGE_DAYS_TO_READ_A_BOOK,
                AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR,
                AVERAGE_BOOKS_READ_PER_MONTH));
        REQUIRED_FIELDS_FOR_STATISTICS.put(BookField.RATING, newArrayList(
                AVERAGE_RATING,
                RATINGS_DISTRIBUTION,
                AVERAGE_RATING_FOR_AUTHORS));
        REQUIRED_FIELDS_FOR_STATISTICS.put(BookField.PUBLICATION_YEAR, newArrayList(
                MOST_BOOKS_READ_BY_PUBLISHED_DECADE,
                AUTHOR_TITLE_AND_PUBLICATION_YEAR));
        REQUIRED_FIELDS_FOR_STATISTICS.put(BookField.RATINGS_COUNT, newArrayList(MOST_POPULAR_AUTHORS_BY_AVERAGE_NUMBER_OF_RATINGS));
        REQUIRED_FIELDS_FOR_STATISTICS.put(BookField.AVERAGE_RATING, newArrayList(MOST_POPULAR_AUTHORS_BY_AVERAGE_NUMBER_OF_RATINGS));
    }

    public List<Statistic> getAvailableStatistics(final List<Book> books) {
        final List<BookField> missingFields = BOOK_FIELD_VALIDATOR.getMissingFields(books).values().stream()
                .flatMap(Collection::stream)
                .distinct()
                .collect(toList());

        final List<Statistic> availableStatistics = REQUIRED_FIELDS_FOR_STATISTICS.entrySet().stream()
                .filter(entry -> !missingFields.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .distinct()
                .collect(toList());

        return Stream.concat(
                availableStatistics.stream(),
                ALWAYS_AVAILABLE_STATISTIC_NAMES.stream())
                .collect(toList());
    }
}
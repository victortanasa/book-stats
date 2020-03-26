import static model.Statistic.*;

import model.Book;
import service.BookLoaderService;
import service.StatisticsService;
import utils.PrinterUtils;

import java.util.List;
import java.util.stream.Stream;

public class BookStats {

    private static final String MY_USER_ID = "60626198";

    private static final BookLoaderService BOOK_LOADER_SERVICE = new BookLoaderService(MY_USER_ID);

    public static void main(final String[] args) {
        final List<Book> books = BOOK_LOADER_SERVICE.loadBooks(BookLoaderService.Source.GOODREADS);

        final StatisticsService statisticsService = new StatisticsService(books);

        Stream.of(MOST_READ_AUTHORS_BY_BOOK_COUNT,
                MOST_READ_AUTHORS_BY_PAGE_COUNT,
                AVERAGE_PAGE_NUMBER_FOR_AUTHORS,
                MOST_BOOKS_READ_BY_PUBLISHED_DECADE,
                MOST_POPULAR_AUTHORS_BY_AVERAGE_NUMBER_OF_RATINGS,
                AVERAGE_RATING_FOR_AUTHORS,
                AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR,
                BOOKS_READ_PER_MONTH,
                PAGES_READ_PER_MONTH,
                RATINGS_DISTRIBUTION,
                MOST_POPULAR_SHELVES)
                .forEach(statistic -> PrinterUtils.printMapStatistic(statistic, statisticsService.getMapStatistic(statistic)));

        Stream.of(AVERAGE_RATING,
                AVERAGE_DAYS_TO_READ_A_BOOK,
                AVERAGE_PAGES_READ_PER_MONTH,
                AVERAGE_BOOKS_READ_PER_MONTH)
                .forEach(statistic -> PrinterUtils.printSingleValueStatistic(statistic, statisticsService.getSingeValueStatistic(statistic)));
    }

}
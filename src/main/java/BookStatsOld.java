import static model.Statistic.*;

import model.Book;
import model.sorting.SortBy;
import model.sorting.SortOrder;
import service.StatisticsServiceOld;
import utils.BookLoader;
import utils.PrinterUtils;

import java.util.Set;
import java.util.stream.Stream;

public class BookStatsOld {

    public static void main(final String[] args) {
        final Set<Book> library = BookLoader.loadBooksFromFile();

        PrinterUtils.printBooks(library, SortBy.RATING, SortOrder.ASC);

        final StatisticsServiceOld statisticsServiceOld = new StatisticsServiceOld(library);

        Stream.of(MOST_READ_AUTHORS_BY_PAGE_COUNT,
                MOST_READ_AUTHORS_BY_BOOK_COUNT,
                MOST_BOOKS_READ_BY_GENRE,
                MOST_BOOKS_READ_BY_PUBLISHED_DECADE,
                TOTAL_RATINGS,
                AUTHORS_WITH_MOST_FAVOURITES,
                AVERAGE_RATING_FOR_AUTHORS,
                AVERAGE_PAGE_NUMBER_FOR_AUTHORS,
                BOOKS_READ_PER_MONTH,
                PAGES_READ_PER_MONTH,
                AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR)
                .forEach(statistic -> PrinterUtils.printMapStatistic(statistic, statisticsServiceOld.getMapStatistic(statistic)));

        Stream.of(SHORTEST_BOOKS, LONGEST_BOOKS)
                .forEach(statistic -> PrinterUtils.printListStatistic(statistic, statisticsServiceOld.getListStatistic(statistic, 8)));

        Stream.of(AVERAGE_DAYS_TO_READ_A_BOOK,
                AVERAGE_PAGES_READ_PER_MONTH,
                AVERAGE_BOOKS_READ_PER_MONTH)
                .forEach(statistic -> PrinterUtils.printSingleValueStatistic(statistic, statisticsServiceOld.getSingeValueStatistic(statistic)));
    }

}
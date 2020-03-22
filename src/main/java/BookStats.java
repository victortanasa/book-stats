import static model.Statistic.*;

import model.Book;
import model.SortBy;
import model.SortOrder;
import service.StatisticsService;
import utils.BookLoader;
import utils.PrinterUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BookStats {

    public static void main(final String[] args) {
        final Set<Book> library = BookLoader.loadBooksFromFile();

        PrinterUtils.printBooks(library, SortBy.RATING, SortOrder.ASC);

        final StatisticsService statisticsService = new StatisticsService(library);

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
                .forEach(statistic -> PrinterUtils.printStatistic(statistic, statisticsService.getStatistic(statistic)));

        final Double averageDaysToReadABook = statisticsService.getAverageDaysToReadABook();
        PrinterUtils.simplePrint(AVERAGE_DAYS_TO_READ_A_BOOK + ": " + averageDaysToReadABook);

        System.out.println("-------------------------");

        final List<String> shortestBooks = statisticsService.getShortestBooks(5);
        shortestBooks.forEach(PrinterUtils::simplePrint);

        final List<String> longestBooks = statisticsService.getLongestBooks(5);
        longestBooks.forEach(PrinterUtils::simplePrint);
    }

}
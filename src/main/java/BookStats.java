import static model.Statistic.*;

import model.Book;
import model.SortBy;
import model.SortOrder;
import service.StatisticsService;
import utils.BookLoader;
import utils.PrinterUtils;

import java.util.Set;
import java.util.stream.Stream;

public class BookStats {

    public static void main(final String[] args) {
        final Set<Book> library = BookLoader.loadBooksFromFile();

        PrinterUtils.printBooks(library, SortBy.RATING, SortOrder.ASC);

        final StatisticsService statisticsService = new StatisticsService(library);

        Stream.of(MOST_READ_AUTHORS_BY_PAGE_COUNT,
                MOST_READ_AUTHORS_BY_BOOK_COUNT,
                MOST_READ_GENRES,
                BOOKS_BY_DECADE,
                BOOKS_BY_RATING,
                AUTHORS_WITH_MOST_FAVOURITES,
                AVERAGE_RATING_FOR_AUTHORS,
                AVERAGE_PAGE_NUMBER_FOR_AUTHORS,
                BOOKS_PER_MONTH,
                PAGES_PER_MONTH)
                .forEach(statistic -> PrinterUtils.printStatistic(statistic, statisticsService.getStatistic(statistic)));
    }

}
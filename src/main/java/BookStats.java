import static model.Statistic.*;

import model.Book;
import service.StatisticsService;
import utils.BookLoader;
import utils.PrinterUtils;

import java.util.Set;
import java.util.stream.Stream;

public class BookStats {

    public static void main(final String[] args) {
        final Set<Book> library = BookLoader.loadBooksFromFile();

        PrinterUtils.printBooks(library);

        final StatisticsService statisticsService = new StatisticsService(library);

        Stream.of(MOST_READ_AUTHORS_BY_BOOKS_READ,
                MOST_READ_AUTHORS_BY_PAGES_READ,
                MOST_READ_GENRES,
                BOOKS_BY_DECADE,
                BOOKS_BY_RATING)
                .forEach(statistic -> PrinterUtils.printStatistic(statistic, statisticsService.getStatistic(statistic)));
    }

}
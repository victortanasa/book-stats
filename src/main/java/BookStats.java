import static model.Statistic.*;

import model.GoodReadsBook;
import service.BookLoader;
import service.StatisticsService;
import utils.PrinterUtils;

import java.util.List;
import java.util.stream.Stream;

public class BookStats {

    private static final BookLoader BOOK_LOADER = new BookLoader();

    public static void main(final String[] args) {
        final List<GoodReadsBook> books = BOOK_LOADER.loadBooks(BookLoader.Source.STORAGE);

        final StatisticsService statisticsService = new StatisticsService(books);

        Stream.of(MOST_BOOKS_READ_BY_PUBLISHED_DECADE,
                BOOKS_READ_PER_MONTH,
                PAGES_READ_PER_MONTH)
                .forEach(statistic -> PrinterUtils.printMapStatistic(statistic, statisticsService.getMapStatistic(statistic)));
    }

}
import static model.Statistic.*;

import model.Book;
import model.Printer;

import java.util.Set;
import java.util.stream.Stream;

public class BookStats {

    public static void main(final String[] args) {
        final Set<Book> books = BookLoader.loadBooksFromFile();

        Printer.printBooks(books);

        Stream.of(MOST_READ_AUTHORS_BY_BOOKS_READ,
                MOST_READ_AUTHORS_BY_PAGES_READ,
                MOST_READ_GENRES,
                BOOKS_BY_DECADE,
                BOOKS_BY_RATING)
                .forEach(statistic -> Printer.printStatistic(statistic, StatisticsService.getStatistic(statistic)));
    }

}
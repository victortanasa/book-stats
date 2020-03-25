import model.GoodReadsBook;
import service.BookLoader;

import java.util.List;

public class BookStats {

    private static final BookLoader BOOK_LOADER = new BookLoader();

    public static void main(final String[] args) {
        final List<GoodReadsBook> books = BOOK_LOADER.loadBooks(BookLoader.Source.STORAGE);
        books.forEach(System.out::println);
    }




}
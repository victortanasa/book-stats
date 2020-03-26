package service;

import static java.util.stream.Collectors.toList;
import static service.BookLoaderService.Source.GOODREADS;

import model.Book;
import model.BookField;
import model.MissingDetails;
import service.api.GoodReadsAPIService;
import service.processing.BookFieldFiller;
import service.processing.BookFieldValidator;
import service.processing.BookFilter;
import utils.PrinterUtils;

import java.util.List;
import java.util.Map;

public class BookLoaderService {

    private static final GoodReadsAPIService GOOD_READS_API_SERVICE = new GoodReadsAPIService();
    private static final BookFieldValidator BOOK_FIELD_VALIDATOR = new BookFieldValidator();
    private static final BookFieldFiller BOOK_FIELD_FILLER = new BookFieldFiller();
    private static final StorageService STORAGE_SERVICE = new StorageService();
    private static final BookFilter BOOK_FILTER = new BookFilter();

    public List<Book> loadBooks(final Source source) {
        return source.equals(GOODREADS) ? getBooksFromGoodReads() : STORAGE_SERVICE.loadBooks();
    }

    private List<Book> getBooksFromGoodReads() {
        final Integer numberOfBooksToRetrieve = GOOD_READS_API_SERVICE.getNumberOfBooksToRetrieve();
        final List<Book> readBooks = GOOD_READS_API_SERVICE.getAllBooksRead(numberOfBooksToRetrieve);
        final List<Book> wantedBooks = BOOK_FILTER.filterUnwantedBooks(readBooks);

        final List<Book> booksWithAllDataLoaded = wantedBooks.stream()
                .map(BookLoaderService::setAdditionalFields)
                .collect(toList());

        final Map<Book, List<BookField>> missingFieldsMap = BOOK_FIELD_VALIDATOR.getMissingFields(booksWithAllDataLoaded);

        final List<Book> books = BOOK_FIELD_FILLER.fillMissingFieldsForBooks(missingFieldsMap);

        STORAGE_SERVICE.saveBooks(books);

        PrinterUtils.printMissingData(BOOK_FIELD_VALIDATOR.getMissingFields(books));

        return books;
    }

    private static Book setAdditionalFields(final Book book) {
        final MissingDetails missingBookDetails = GOOD_READS_API_SERVICE.getMissingBookDetails(book.getId());
        return book
                .withPublicationYear(missingBookDetails.getPublicationYear())
                .withShelves(missingBookDetails.getShelves());
    }

    public enum Source {
        GOODREADS,
        STORAGE
    }
}
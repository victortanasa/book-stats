package service;

import static java.util.stream.Collectors.toList;
import static service.BookLoaderService.Source.GOODREADS;

import model.Book;
import model.BookField;
import model.MissingDetails;
import model.Shelve;
import service.api.GoodReadsAPIService;
import service.processing.BookFieldFiller;
import service.processing.BookFieldValidator;
import service.processing.BookFilter;
import utils.PrinterUtils;

import java.util.List;
import java.util.Map;

public class BookLoaderService {

    private static final BookFieldValidator BOOK_FIELD_VALIDATOR = new BookFieldValidator();
    private static final BookFieldFiller BOOK_FIELD_FILLER = new BookFieldFiller();
    private static final StorageService STORAGE_SERVICE = new StorageService();
    private static final BookFilter BOOK_FILTER = new BookFilter();

    private static final GoodReadsAPIService GOOD_READS_API_SERVICE = new GoodReadsAPIService();

    private final String userId;

    public BookLoaderService(final String userId) {
        this.userId = userId;
    }

    public List<Book> loadBooks(final Source source) {
        return source.equals(GOODREADS) ? getBooksFromGoodReads(userId) : STORAGE_SERVICE.loadBooks(userId);
    }

    private List<Book> getBooksFromGoodReads(final String userId) {
        final List<Shelve> shelves = GOOD_READS_API_SERVICE.getNumberOfBooksToRetrieve(userId);
        final List<Book> readBooks = GOOD_READS_API_SERVICE.getBooksForShelve(userId, getShelve(shelves, "read"));
        final List<Book> favoriteBooks = GOOD_READS_API_SERVICE.getBooksForShelve(userId, getShelve(shelves, "favorites"));
        final List<Book> dnfBooks = GOOD_READS_API_SERVICE.getBooksForShelve(userId, getShelve(shelves, "dnf"));
        final List<Book> wantedBooks = BOOK_FILTER.filterUnwantedBooks(readBooks);

        final List<Book> booksWithAllDataLoaded = wantedBooks.stream()
                .map(this::setAdditionalFields)
                .collect(toList());

        final Map<Book, List<BookField>> missingFieldsMap = BOOK_FIELD_VALIDATOR.getMissingFields(booksWithAllDataLoaded);

        final List<Book> books = BOOK_FIELD_FILLER.fillMissingFieldsForBooks(userId, missingFieldsMap);

        STORAGE_SERVICE.saveBooks(userId, books);

        PrinterUtils.printMissingData(BOOK_FIELD_VALIDATOR.getMissingFields(books));

        return books;
    }

    private Book setAdditionalFields(final Book book) {
        final MissingDetails missingBookDetails = GOOD_READS_API_SERVICE.getMissingBookDetails(book.getId());
        return book.withPublicationYear(missingBookDetails.getPublicationYear()).withShelves(missingBookDetails.getShelves());
    }

    public enum Source {
        GOODREADS,
        STORAGE
    }

    private Shelve getShelve(final List<Shelve> shelves, final String shelveName) {
        return shelves.stream()
                .filter(shelve -> shelveName.equals(shelve.getName()))
                .findFirst()
                .orElse(null);
    }
}
package service;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static service.BookLoaderService.Source.GOODREADS;

import model.Book;
import model.MissingDetails;
import model.enums.BookField;
import model.enums.Shelve;
import org.apache.commons.lang3.tuple.Pair;
import service.api.GoodReadsAPIService;
import service.processing.BookFieldFiller;
import service.processing.BookFieldValidator;
import service.processing.BookFilter;
import utils.PrinterUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Map<Shelve, List<Book>> loadBooks(final Source source) {
        return source.equals(GOODREADS) ? getBooksFromGoodReads(userId) : getBooksFromStorage(userId);
    }

    private Map<Shelve, List<Book>> getBooksFromGoodReads(final String userId) {
        final Map<Shelve, Integer> shelveMap = GOOD_READS_API_SERVICE.getBookCountToRetrievePerShelf(userId);

        shelveMap.forEach((shelve, bookCount) -> {
            final List<Book> books = GOOD_READS_API_SERVICE.getBooksForShelve(userId, shelve, bookCount);
            final List<Book> filteredBooks = BOOK_FILTER.filterUnwantedBooks(books);

            final List<Book> booksWithAdditionalData = filteredBooks.stream()
                    .map(this::setAdditionalFields)
                    .collect(toList());

            final List<Book> booksWithDataFromStorage = fillBookDataFromStorage(userId, booksWithAdditionalData);

            PrinterUtils.printMissingData(shelve, BOOK_FIELD_VALIDATOR.getMissingFields(books));


            STORAGE_SERVICE.saveBooks(userId, shelve, booksWithDataFromStorage);
        });

        return getBooksPerShelve(userId, shelveMap.keySet());
    }

    private Map<Shelve, List<Book>> getBooksFromStorage(final String userId) {
        return getBooksPerShelve(userId, Stream.of(Shelve.values()).collect(toSet()));
    }

    //TODO: check code for stuff like this, eliminate helper objects
    private Map<Shelve, List<Book>> getBooksPerShelve(final String userId, final Set<Shelve> shelves) {
        return shelves.stream()
                .map(shelf -> Pair.of(shelf, STORAGE_SERVICE.loadBooks(userId, shelf)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private List<Book> fillBookDataFromStorage(final String userId, final List<Book> books) {
        final Map<Book, List<BookField>> missingFieldsMap = BOOK_FIELD_VALIDATOR.getMissingFields(books);

        return BOOK_FIELD_FILLER.fillMissingFieldsForBooks(userId, missingFieldsMap);
    }

    private Book setAdditionalFields(final Book book) {
        final MissingDetails missingBookDetails = GOOD_READS_API_SERVICE.getMissingBookDetails(book.getId());
        return book.withPublicationYear(missingBookDetails.getPublicationYear()).withShelves(missingBookDetails.getShelves());
    }

    public enum Source {
        GOODREADS,
        STORAGE
    }

}
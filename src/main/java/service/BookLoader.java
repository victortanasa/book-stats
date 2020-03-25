package service;

import static java.util.stream.Collectors.toList;
import static service.BookLoader.Source.GOODREADS;

import model.BookField;
import model.GoodReadsBook;
import model.MissingDetails;
import service.api.GoodReadsAPIService;
import service.processing.BookFieldFiller;
import service.processing.BookFieldValidator;
import service.processing.BookFilter;

import java.util.List;
import java.util.Map;

public class BookLoader {

    private static final GoodReadsAPIService GOOD_READS_API_SERVICE = new GoodReadsAPIService();
    private static final BookFieldValidator BOOK_FIELD_VALIDATOR = new BookFieldValidator();
    private static final BookFieldFiller BOOK_FIELD_FILLER = new BookFieldFiller();
    private static final StorageService STORAGE_SERVICE = new StorageService();
    private static final BookFilter BOOK_FILTER = new BookFilter();

    public List<GoodReadsBook> loadBooks(final Source source) {
        return source.equals(GOODREADS) ? getBooksFromGoodReads() : STORAGE_SERVICE.loadBooks();
    }

    private List<GoodReadsBook> getBooksFromGoodReads() {
        final Integer numberOfBooksToRetrieve = GOOD_READS_API_SERVICE.getNumberOfBooksToRetrieve();
        final List<GoodReadsBook> readBooks = GOOD_READS_API_SERVICE.getAllBooksRead(numberOfBooksToRetrieve);
        final List<GoodReadsBook> wantedBooks = BOOK_FILTER.filterUnwantedBooks(readBooks);

        final List<GoodReadsBook> booksWithAllDataLoaded = wantedBooks.stream()
                .map(BookLoader::setAdditionalFields)
                .collect(toList());

        final Map<GoodReadsBook, List<BookField>> missingFieldsMap = BOOK_FIELD_VALIDATOR.getMissingFields(booksWithAllDataLoaded);

        final List<GoodReadsBook> goodReadsBooks = BOOK_FIELD_FILLER.fillMissingFieldsForBooks(missingFieldsMap);

        STORAGE_SERVICE.saveBooks(goodReadsBooks);

        return goodReadsBooks;
    }

    private static GoodReadsBook setAdditionalFields(final GoodReadsBook book) {
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
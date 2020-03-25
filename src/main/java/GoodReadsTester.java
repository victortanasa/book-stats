import static java.util.stream.Collectors.toList;

import model.BookField;
import model.GoodReadsBook;
import model.MissingDetails;
import service.api.GoodReadsAPIService;
import service.processing.BookFieldFiller;
import service.processing.BookFieldValidator;
import service.processing.BookFilter;
import utils.PrinterUtils;

import java.util.List;
import java.util.Map;

public class GoodReadsTester {

    private static final GoodReadsAPIService GOOD_READS_API_SERVICE = new GoodReadsAPIService();
    private static final BookFieldValidator BOOK_FIELD_VALIDATOR = new BookFieldValidator();
    private static final BookFieldFiller BOOK_FIELD_FILLER = new BookFieldFiller();
    private static final BookFilter BOOK_FILTER = new BookFilter();

    public static void main(final String[] args) {
        final Integer numberOfBooksToRetrieve = GOOD_READS_API_SERVICE.getNumberOfBooksToRetrieve();
        final List<GoodReadsBook> readBooks = GOOD_READS_API_SERVICE.getAllBooksRead(numberOfBooksToRetrieve);
        final List<GoodReadsBook> wantedBooks = BOOK_FILTER.filterUnwantedBooks(readBooks);

        final List<GoodReadsBook> booksWithAllDataLoaded = wantedBooks.stream()
                .map(GoodReadsTester::setAdditionalFields)
                .collect(toList());

        final Map<GoodReadsBook, List<BookField>> missingFieldsMap = BOOK_FIELD_VALIDATOR.getMissingFields(booksWithAllDataLoaded);

        PrinterUtils.printMissingData(missingFieldsMap);

        final List<GoodReadsBook> booksFilledWithStoredDetails = BOOK_FIELD_FILLER.fillMissingFieldsForBooks(missingFieldsMap);

        booksFilledWithStoredDetails.forEach(System.out::println);
    }

    private static GoodReadsBook setAdditionalFields(final GoodReadsBook book) {
        final MissingDetails missingBookDetails = GOOD_READS_API_SERVICE.getMissingBookDetails(book.getId());
        return book
                .withPublicationYear(missingBookDetails.getPublicationYear())
                .withShelves(missingBookDetails.getShelves());
    }

    //TODO:
//    Id: 35014337
//    Title: We Are Legion (We Are Bob)
//    Authors: [Dennis E. Taylor]
//    Missing Fields: [FORMAT, PAGE_NUMBER]

}
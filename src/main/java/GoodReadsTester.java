import static java.util.stream.Collectors.toList;

import model.GoodReadsBook;
import model.MissingBookFields;
import model.MissingDetails;
import service.api.GoodReadsAPIService;
import service.processing.BookFieldFiller;
import service.processing.BookFieldValidator;
import service.processing.BookFilter;
import utils.PrinterUtils;

import java.util.List;

public class GoodReadsTester {

    private static final GoodReadsAPIService GOOD_READS_API_SERVICE = new GoodReadsAPIService();
    private static final BookFieldValidator BOOK_FIELD_VALIDATOR = new BookFieldValidator();
    private static final BookFieldFiller BOOK_FIELD_FILLER = new BookFieldFiller();
    private static final BookFilter BOOK_FILTER = new BookFilter();

    public static void main(final String[] args) {
        final Integer numberOfBooksRead = GOOD_READS_API_SERVICE.getNumberOfBooksToRetrieve();
        final List<GoodReadsBook> readBooks = GOOD_READS_API_SERVICE.getAllBooksRead(numberOfBooksRead);
        final List<GoodReadsBook> wantedBooks = BOOK_FILTER.filterUnwantedBooks(readBooks);

        System.out.println("wanted books size: " + wantedBooks.size());

        final List<GoodReadsBook> booksWithAllDataLoaded = wantedBooks.stream()
                .map(GoodReadsTester::setAdditionalFields)
                .collect(toList());

        final List<MissingBookFields> missingBookFields = BOOK_FIELD_VALIDATOR.getMissingData(booksWithAllDataLoaded);
        final List<GoodReadsBook> booksFilledWithStoredDetails = BOOK_FIELD_FILLER.fillMissingFieldsForBooks(missingBookFields);

        PrinterUtils.printMissingData(BOOK_FIELD_VALIDATOR.getMissingData(booksFilledWithStoredDetails));

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
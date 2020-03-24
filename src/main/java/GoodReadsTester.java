import static java.util.stream.Collectors.toList;

import model.GoodReadsBook;
import model.MissingDetails;
import service.BookExcluder;
import service.BookValidator;
import service.GoodReadsRequestService;
import utils.PrinterUtils;

import java.util.List;

public class GoodReadsTester {

    public static void main(final String[] args) {
        final GoodReadsRequestService requestService = new GoodReadsRequestService();

        final Integer numberOfBooksRead = requestService.getNumberOfBooksRead();

        final List<GoodReadsBook> readBooks = requestService.getAllBooksRead(numberOfBooksRead);

        final List<GoodReadsBook> filteredBooks = BookExcluder.removeExcludedBooks(readBooks);

        final List<GoodReadsBook> finalBooks = filteredBooks.stream().map(book -> {
            final MissingDetails missingBookDetails = requestService.getMissingBookDetails(book.getId());
            return book.withPublicationYear(missingBookDetails.getPublicationYear())
                    .withShelves(missingBookDetails.getShelves());
        }).collect(toList());
//
        System.out.println("Library size is : " + filteredBooks.size());
//
        finalBooks.forEach(System.out::println);

        final BookValidator bookValidator = new BookValidator();
        final List<BookValidator.MissingDataResult> missingData = bookValidator.getMissingData(filteredBooks);
        PrinterUtils.printMissingData(missingData);
    }

}
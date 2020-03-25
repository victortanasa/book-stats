import static java.util.stream.Collectors.toList;

import model.GoodReadsBook;
import model.MissingDataResult;
import model.MissingDetails;
import service.BookExcluder;
import service.BookValidator;
import service.GoodReadsRequestService;
import service.MissingBookDataFiller;
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
            return book
                    .withPublicationYear(missingBookDetails.getPublicationYear())
                    .withShelves(missingBookDetails.getShelves());
        }).collect(toList());

        System.out.println("Library size is : " + finalBooks.size());

        finalBooks.forEach(System.out::println);

        final BookValidator bookValidator = new BookValidator();
        final List<MissingDataResult> missingData = bookValidator.getMissingData(filteredBooks);
        PrinterUtils.printMissingData(missingData);

        final MissingBookDataFiller missingBookDataFiller = new MissingBookDataFiller();
        final List<GoodReadsBook> booksWithAllDataLoaded = missingBookDataFiller.fillMissingBooksData(missingData);

        PrinterUtils.printMissingData(bookValidator.getMissingData(booksWithAllDataLoaded));
    }

    //TODO:
//    Id: 35014337
//    Title: We Are Legion (We Are Bob)
//    Authors: [Dennis E. Taylor]
//    Missing Fields: [FORMAT, PAGE_NUMBER]

}
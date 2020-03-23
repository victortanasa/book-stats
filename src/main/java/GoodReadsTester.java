import static java.util.stream.Collectors.toList;

import model.GoodReadsBook;
import org.apache.commons.lang3.StringUtils;
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

        final List<GoodReadsBook> booksWithPublicationYear = filteredBooks.stream()
                .map(book -> {
                    final Integer originalPublicationYear = requestService.getPublicationYear(!StringUtils.isBlank(book.getIsbn()) ? book.getIsbn() : book.getTitle(), book.getId());
                    return book.withPublicationYear(originalPublicationYear);
                }).collect(toList());

        System.out.println("Size is : " + filteredBooks.size());

        //TODO: investigate publication year (most likely absent without isbn), page number
        booksWithPublicationYear.forEach(System.out::println);

        final BookValidator bookValidator = new BookValidator();
        final List<BookValidator.MissingDataResult> missingData = bookValidator.getMissingData(filteredBooks);
        PrinterUtils.printMissingData(missingData);
    }

}
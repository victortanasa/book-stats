import static java.util.stream.Collectors.toList;

import model.GoodReadsBook;
import org.apache.commons.lang3.StringUtils;
import service.GoodReadsRequestService;

import java.util.List;

public class GoodReadsTester {

    public static void main(final String[] args) {
        final GoodReadsRequestService requestService = new GoodReadsRequestService();

        final Integer numberOfBooksRead = requestService.getNumberOfBooksRead();

        final List<GoodReadsBook> readBooks = requestService.getAllBooksRead(numberOfBooksRead);

        final List<GoodReadsBook> booksWithPublicationYear = readBooks.stream()
                .map(book -> {
                    final Integer originalPublicationYear = requestService.getPublicationYear(!StringUtils.isBlank(book.getIsbn()) ? book.getIsbn() : book.getTitle(), book.getId());
                    if (originalPublicationYear == null) {
                        System.out.println(book);
                    }

                    return book.withPublicationYear(originalPublicationYear);
                }).collect(toList());

        System.out.println("Size is : " + readBooks.size());

        //TODO: investigate publication year (most likely absent without isbn), page number

        booksWithPublicationYear.forEach(System.out::println);

    }

}
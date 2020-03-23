import model.GoodReadsBook;
import service.GoodReadsRequestService;

import java.util.List;

public class GoodReadsTester {

    public static void main(final String[] args) {
        final GoodReadsRequestService requestService = new GoodReadsRequestService();

        final Integer numberOfBooksRead = requestService.getNumberOfBooksRead();

        final List<GoodReadsBook> readBooks = requestService.getAllBooksRead(numberOfBooksRead);

        System.out.println("Size is : " + readBooks.size());

        readBooks.forEach(System.out::println);

    }

}
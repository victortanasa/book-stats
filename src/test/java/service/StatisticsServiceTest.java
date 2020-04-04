package service;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static service.Statistics.AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR;
import static service.Statistics.PAGES_READ_PER_MONTH_MEDIAN;

import model.Book;
import model.enums.ShelveName;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StatisticsServiceTest {

    private static final String MY_USER_ID = "60626198";

    @Test
    public void pagesReadPerMonthMedianTest() {
        final StatisticsService statisticsService = new StatisticsService(getBooksWithDatesAndPageNumber());

        final Map<String, ?> mapStatistic = statisticsService.getMapStatistic(PAGES_READ_PER_MONTH_MEDIAN);
        assertThat(mapStatistic.get("2020-01"), is(40D));
        assertThat(mapStatistic.get("2020-02"), is(110D));
        assertThat(mapStatistic.get("2020-03"), is(150D));
    }

    @Test
    public void averageDaysToReadABookPerAuthorTest() {
        final StatisticsService statisticsService = new StatisticsService(getBooksWithDatesAndPageNumber());

        final Map<String, ?> mapStatistic = statisticsService.getMapStatistic(AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR);
        assertThat(mapStatistic.get("author"), is(3.6666666666666665));
    }

    @Test
    public void asimovTest() {
        final BookLoaderService bookLoaderService = new BookLoaderService(MY_USER_ID);
        final Map<ShelveName, List<Book>> shelves = bookLoaderService.loadBooks(BookLoaderService.Source.STORAGE);
        final List<Book> readBooks = shelves.get(ShelveName.READ);
        final List<Book> asimovBooks = readBooks.stream()
                .filter(book -> "Isaac Asimov".equalsIgnoreCase(book.getAuthorAsString()))
                .collect(toList());
        asimovBooks.forEach(book -> System.out.println(String.format("Title: %s, Read duration: %s", book.getTitle(), book.getDaysReadIn())));

        //"Isaac Asimov","8.79"
    }

    private static List<Book> getBooksWithDatesAndPageNumber() {
        final Book book_1 = new Book();

        //read in 5 days, 100 pages -> 20 pages / day
        book_1.setPageNumber(100);
        book_1.setDateStarted(LocalDate.of(2020, 1, 30));
        book_1.setDateFinished(LocalDate.of(2020, 2, 3));
        book_1.setAuthors(newArrayList("author"));

        final Book book_2 = new Book();

        //read in 2 days, 100 pages -> 50 pages / day
        book_2.setPageNumber(100);
        book_2.setDateStarted(LocalDate.of(2020, 2, 29));
        book_2.setDateFinished(LocalDate.of(2020, 3, 1));
        book_2.setAuthors(newArrayList("author"));

        final Book book_3 = new Book();

        //read in 4 days, 100 pages -> 25 pages / day
        book_3.setPageNumber(100);
        book_3.setDateStarted(LocalDate.of(2020, 3, 3));
        book_3.setDateFinished(LocalDate.of(2020, 3, 6));
        book_3.setAuthors(newArrayList("author"));

        return newArrayList(book_1, book_2, book_3);
    }

}
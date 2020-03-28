package service;

import static com.google.common.collect.Lists.newArrayList;
import static model.enums.Statistic.PAGES_READ_PER_MONTH_MEDIAN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import model.Book;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StatisticsServiceTest {

    @Test
    public void pagesReadPerMonthMedianTest() {
        final StatisticsService statisticsService = new StatisticsService(getBooksWithDatesAndPageNumber());

        final Map<String, ?> mapStatistic = statisticsService.getMapStatistic(PAGES_READ_PER_MONTH_MEDIAN);
        assertThat(mapStatistic.get("2020-01"), is(50D));
        assertThat(mapStatistic.get("2020-02"), is(100D));
        assertThat(mapStatistic.get("2020-03"), is(150D));
    }

    private static List<Book> getBooksWithDatesAndPageNumber() {
        final Book book_1 = new Book();

        book_1.setPageNumber(100);
        book_1.setDateStarted(LocalDate.of(2020, 1, 31));
        book_1.setDateFinished(LocalDate.of(2020, 2, 1));

        final Book book_2 = new Book();

        book_2.setPageNumber(100);
        book_2.setDateStarted(LocalDate.of(2020, 2, 29));
        book_2.setDateFinished(LocalDate.of(2020, 3, 1));

        final Book book_3 = new Book();

        book_3.setPageNumber(100);
        book_3.setDateStarted(LocalDate.of(2020, 3, 3));
        book_3.setDateFinished(LocalDate.of(2020, 3, 10));

        return newArrayList(book_1, book_2, book_3);
    }

}
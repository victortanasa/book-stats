import static model.Statistic.*;

import model.Statistic;

import java.util.Map;
import java.util.stream.Stream;

public class BookStats {

    private static final String SEPARATOR = "-------------------------------";
    private static final String NEWLINE = "\n";

    public static void main(final String[] args) {
        Stream.of(MOST_READ_AUTHORS_BY_BOOKS_READ, MOST_READ_AUTHORS_BY_PAGES_READ, MOST_READ_GENRES, BOOKS_BY_DECADE)
                .forEach(statistic -> printStatistic(statistic, StatisticsService.getStatistic(statistic)));
    }

    private static void printStatistic(final Statistic statistic, final Map<String, Long> map) {
        System.out.println(statistic.getStringValue() + NEWLINE);

        map.entrySet().forEach(System.out::println);

        System.out.println(SEPARATOR);
    }
}
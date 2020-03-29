import static model.enums.StatisticType.MAP;
import static model.enums.StatisticType.SINGLE_VALUE;

import model.Book;
import model.Statistic;
import model.enums.ShelveName;
import service.AvailableStatisticsService;
import service.BookLoaderService;
import service.StatisticsService;
import utils.PrinterUtils;

import java.util.List;
import java.util.Map;

public class BookStats {

    private static final String HERNANDO_USER_ID = "86868346";
    private static final String FLAVIA_USER_ID = "29721538";
    private static final String IOANA_USER_ID = "19636467";
    private static final String ILIE_USER_ID = "70582685";
    private static final String RADU_USER_ID = "748293";
    private static final String MY_USER_ID = "60626198";

    private static final AvailableStatisticsService AVAILABLE_STATISTICS_SERVICE = new AvailableStatisticsService();
    private static final BookLoaderService BOOK_LOADER_SERVICE = new BookLoaderService(MY_USER_ID);

    public static void main(final String[] args) {
        final Map<ShelveName, List<Book>> shelvesAndBook = BOOK_LOADER_SERVICE.loadBooks(BookLoaderService.Source.STORAGE);

        final List<Statistic> availableStatistics = AVAILABLE_STATISTICS_SERVICE.getAvailableStatistics(shelvesAndBook.get(ShelveName.READ));

        final StatisticsService statisticsService = new StatisticsService(shelvesAndBook.get(ShelveName.READ));

        availableStatistics.stream()
                .filter(statistic -> MAP.equals(statistic.getType()))
                .forEach(statistic -> PrinterUtils.printMapStatistic(statistic, statisticsService.getMapStatistic(statistic)));

        availableStatistics.stream()
                .filter(statistic -> SINGLE_VALUE.equals(statistic.getType()))
                .forEach(statistic -> PrinterUtils.printSingleValueStatistic(statistic, statisticsService.getSingeValueStatistic(statistic)));
    }

}
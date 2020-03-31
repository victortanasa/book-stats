import static model.enums.StatisticType.MAP;
import static service.Statistics.*;

import model.Book;
import model.Statistic;
import model.enums.ShelveName;
import service.AvailableStatisticsService;
import service.BookLoaderService;
import service.StatisticsService;
import service.StorageService;

import java.util.List;
import java.util.Map;

public class BookStats {

    private static final String HERNANDO_USER_ID = "86868346";
    private static final String FLAVIA_USER_ID = "29721538";
    private static final String IOANA_USER_ID = "19636467";
    private static final String ILIE_USER_ID = "70582685";
    private static final String RADU_USER_ID = "748293";
    private static final String MY_USER_ID = "60626198";
    private static final String BEN_USER_ID = "8500357";

    private static final AvailableStatisticsService AVAILABLE_STATISTICS_SERVICE = new AvailableStatisticsService();
    private static final BookLoaderService BOOK_LOADER_SERVICE = new BookLoaderService(MY_USER_ID);
    private static final StorageService STORAGE_SERVICE = new StorageService();

    public static void main(final String[] args) {
        final Map<ShelveName, List<Book>> shelvesAndBook = BOOK_LOADER_SERVICE.loadBooks(BookLoaderService.Source.STORAGE);

        final List<Statistic> availableStatistics = AVAILABLE_STATISTICS_SERVICE.getAvailableStatistics(shelvesAndBook.get(ShelveName.READ));

        final StatisticsService statisticsService = new StatisticsService(shelvesAndBook.get(ShelveName.READ));

        availableStatistics.stream()
                .filter(statistic -> MAP.equals(statistic.getType()))
                .forEach(statistic -> STORAGE_SERVICE.saveStatisticToCsv(statistic, statisticsService.getMapStatistic(statistic)));
//
//        availableStatistics.stream()
//                .filter(statistic -> SINGLE_VALUE.equals(statistic.getType()))
//                .forEach(statistic -> PrinterUtils.printSingleValueStatistic(statistic, statisticsService.getSingeValueStatistic(statistic)));

        //TODO: save combined stats, load again for merge
        final Map<String, ? extends Comparable<?>> authorsByBookCount = statisticsService.getMapStatistic(MOST_READ_AUTHORS_BY_BOOK_COUNT);
        final Map<String, ? extends Comparable<?>> authorsByPageCount = statisticsService.getMapStatistic(MOST_READ_AUTHORS_BY_PAGE_COUNT);

        STORAGE_SERVICE.saveAndMergeStatisticToCsv(MOST_READ_AUTHORS_BY_BOOK_COUNT, MOST_READ_AUTHORS_BY_PAGE_COUNT,
                authorsByBookCount, authorsByPageCount);

        final Map<String, ? extends Comparable<?>> pages = statisticsService.getMapStatistic(PAGES_READ_PER_MONTH);
        final Map<String, ? extends Comparable<?>> pagesMedian = statisticsService.getMapStatistic(PAGES_READ_PER_MONTH_MEDIAN);

        STORAGE_SERVICE.saveAndMergeStatisticToCsv(PAGES_READ_PER_MONTH, PAGES_READ_PER_MONTH_MEDIAN,
                pages, pagesMedian);

    }

}
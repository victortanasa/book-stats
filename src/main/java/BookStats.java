import model.Book;
import model.SortBy;
import model.SortOrder;
import model.Statistic;
import service.StatisticsService2;
import utils.BookLoader;
import utils.PrinterUtils;

import java.util.Map;
import java.util.Set;

public class BookStats {

    public static void main(final String[] args) {
        final Set<Book> library = BookLoader.loadBooksFromFile();

        PrinterUtils.printBooks(library, SortBy.RATING, SortOrder.ASC);

        final StatisticsService2 statisticsService = new StatisticsService2(library);

        final Map<String, Long> authorsWithMostFavourites = statisticsService.getAuthorsWithMostFavourites();
        PrinterUtils.printStatistic(Statistic.AUTHORS_WITH_MOST_FAVOURITES, authorsWithMostFavourites);

        final Map<String, Long> booksByDecade = statisticsService.getBooksByDecade();
        PrinterUtils.printStatistic(Statistic.BOOKS_BY_DECADE, booksByDecade);

        final Map<String, Long> mostReadAuthorsByPageCount = statisticsService.getMostReadAuthorsByPageCount();
        PrinterUtils.printStatistic(Statistic.MOST_READ_AUTHORS_BY_PAGE_COUNT, mostReadAuthorsByPageCount);

        final Map<String, Long> mostReadAuthorsByBookCount = statisticsService.getMostReadAuthorsByBookCount();
        PrinterUtils.printStatistic(Statistic.MOST_READ_AUTHORS_BY_BOOKS_COUNT, mostReadAuthorsByBookCount);

        final Map<String, Double> averageRatingForAuthors = statisticsService.getAverageRatingForAuthors();
        PrinterUtils.printStatistic(Statistic.AVERAGE_RATING_FOR_AUTHORS, averageRatingForAuthors);

        final Map<String, Long> booksByRating = statisticsService.getBooksByRating();
        PrinterUtils.printStatistic(Statistic.BOOKS_BY_RATING, booksByRating);

        final Map<String, Long> mostReadGenres = statisticsService.getMostReadGenres();
        PrinterUtils.printStatistic(Statistic.MOST_READ_GENRES, mostReadGenres);
    }

}
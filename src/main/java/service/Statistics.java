package service;

import static com.google.common.collect.Lists.newArrayList;
import static model.enums.StatisticType.MAP;
import static model.enums.StatisticType.SINGLE_VALUE;
import static model.enums.sort.SortBy.KEY;
import static model.enums.sort.SortBy.VALUE;
import static model.enums.sort.SortOrder.ASC;
import static model.enums.sort.SortOrder.DESC;

import model.Statistic;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Statistics {

    //TODO: arrange by average, most popular etc

    static Statistic MOST_POPULAR_AUTHORS_BY_AVERAGE_NUMBER_OF_RATINGS = new Statistic("Most popular authors by average number of ratings", MAP, VALUE, DESC)
            .withResultLimit(20)
            .withStatisticKeyName("Author")
            .withStatisticValueName("Number of Ratings");

    static Statistic AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR = new Statistic("Average days to read a book per author", MAP, VALUE, DESC)
            .withResultLimit(20)
            .withStatisticKeyName("Author")
            .withStatisticValueName("Days");

    static Statistic MOST_BOOKS_READ_BY_PUBLISHED_DECADE = new Statistic("Most books read by published decade", MAP, VALUE, DESC)
            .withStatisticKeyName("Decade")
            .withStatisticValueName("Books");

    static Statistic MOST_READ_AUTHORS_BY_BOOK_COUNT = new Statistic("Most read authors by book count", MAP, VALUE, DESC)
            .withResultLimit(20)
            .withStatisticKeyName("Author")
            .withStatisticValueName("Book Count");

    static Statistic MOST_READ_AUTHORS_BY_PAGE_COUNT = new Statistic("Most read authors by page count", MAP, VALUE, DESC)
            .withResultLimit(20)
            .withStatisticKeyName("Author")
            .withStatisticValueName("Page Count");

    static Statistic AVERAGE_PAGE_NUMBER_FOR_AUTHORS = new Statistic("Average page number for authors", MAP, VALUE, DESC)
            .withResultLimit(20)
            .withStatisticKeyName("Author")
            .withStatisticValueName("Average Page Count");

    static Statistic AVERAGE_BOOK_LENGTH_PER_YEAR = new Statistic("Average book length per year", MAP, KEY, ASC)
            .withStatisticKeyName("Year")
            .withStatisticValueName("Average Book Length");

    static Statistic AVERAGE_RATING_FOR_AUTHORS = new Statistic("Average rating for authors", MAP, VALUE, DESC)
            .withStatisticKeyName("Author")
            .withStatisticValueName("Average Rating");

    static Statistic AVERAGE_PAGES_READ_PER_DAY_PER_MONTH = new Statistic("Average pages read per day per month", MAP, KEY, ASC)
            .withStatisticKeyName("Month")
            .withStatisticValueName("Average Pages Read Per Day");

    static Statistic PAGES_READ_PER_MONTH_MEDIAN = new Statistic("Pages red per month median", MAP, KEY, ASC)
            .withStatisticKeyName("Month")
            .withStatisticValueName("Pages Read Median");

    static Statistic PAGES_READ_PER_MONTH = new Statistic("Pages red per month", MAP, KEY, ASC)
            .withStatisticKeyName("Month")
            .withStatisticValueName("Pages Read");

    static Statistic BOOKS_READ_PER_MONTH = new Statistic("Books read per month", MAP, KEY, ASC)
            .withStatisticKeyName("Month")
            .withStatisticValueName("Books");

    static Statistic BOOKS_READ_PER_YEAR = new Statistic("Books read per year", MAP, KEY, ASC)
            .withStatisticKeyName("Year")
            .withStatisticValueName("Books");

    static Statistic PAGES_READ_PER_YEAR = new Statistic("Pages read per year", MAP, KEY, ASC)
            .withStatisticKeyName("Year")
            .withStatisticValueName("Pages");

    static Statistic MOST_POPULAR_SHELVES = new Statistic("Most popular shelves", MAP, VALUE, DESC).withResultLimit(12)
            .withStatisticKeyName("Shelve")
            .withStatisticValueName("Genre");

    static Statistic RATINGS_DISTRIBUTION = new Statistic("Ratings distribution", MAP, KEY, DESC)
            .withStatisticKeyName("Rating")
            .withStatisticValueName("Stars");

    static Statistic FORMATS_DISTRIBUTION = new Statistic("Formats distribution", MAP, VALUE, DESC)
            .withStatisticKeyName("Format")
            .withStatisticValueName("Number");

    static Statistic AVERAGE_PAGES_READ_PER_MONTH = new Statistic("Average pages read per month", SINGLE_VALUE);

    static Statistic AVERAGE_BOOKS_READ_PER_MONTH = new Statistic("Average books read per month", SINGLE_VALUE);

    static Statistic AVERAGE_DAYS_TO_READ_A_BOOK = new Statistic("Average days to read a book", SINGLE_VALUE);

    static Statistic NUMBER_OF_AUTHORS_READ = new Statistic("Number of authors read", SINGLE_VALUE);

    static Statistic AVERAGE_RATING = new Statistic("Average rating", SINGLE_VALUE);

    //TODO: implement && add in AvailableStatisticsService
    public static Statistic AUTHORS_WITH_MOST_FAVOURITES = new Statistic("Average rating", MAP, VALUE, DESC);

    public static List<Pair<Statistic, Statistic>> DOUBLE_AXIS_STATISTICS = newArrayList(
            Pair.of(MOST_READ_AUTHORS_BY_BOOK_COUNT, MOST_READ_AUTHORS_BY_PAGE_COUNT));
}
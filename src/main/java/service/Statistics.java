package service;

import static model.enums.StatisticType.MAP;
import static model.enums.StatisticType.SINGLE_VALUE;

import model.Statistic;

public class Statistics {

    static Statistic MOST_POPULAR_AUTHORS_BY_AVERAGE_NUMBER_OF_RATINGS = new Statistic("Most popular authors by average number of ratings", MAP);
    static Statistic AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR = new Statistic("Average days to read a book per author", MAP);
    static Statistic MOST_BOOKS_READ_BY_PUBLISHED_DECADE = new Statistic("Most books read by published decade", MAP);
    static Statistic MOST_READ_AUTHORS_BY_BOOK_COUNT = new Statistic("Most read authors by book count", MAP);
    static Statistic MOST_READ_AUTHORS_BY_PAGE_COUNT = new Statistic("Most read authors by page count", MAP);
    static Statistic AVERAGE_PAGE_NUMBER_FOR_AUTHORS = new Statistic("Average page number for authors", MAP);
    static Statistic PAGES_READ_PER_MONTH_MEDIAN = new Statistic("Pages red per month median", MAP);
    static Statistic AVERAGE_RATING_FOR_AUTHORS = new Statistic("Average rating for authors", MAP);
    static Statistic MOST_POPULAR_SHELVES = new Statistic("Most popular shelves", MAP);
    static Statistic RATINGS_DISTRIBUTION = new Statistic("Ratings distribution", MAP);
    static Statistic FORMATS_DISTRIBUTION = new Statistic("Formats distribution", MAP);
    static Statistic BOOKS_READ_PER_MONTH = new Statistic("Books read per month", MAP);
    static Statistic PAGES_READ_PER_MONTH = new Statistic("Pages red per month", MAP);
    static Statistic BOOKS_READ_PER_YEAR = new Statistic("Books read per year", MAP);
    static Statistic PAGES_READ_PER_YEAR = new Statistic("Pages read per year", MAP);

    static Statistic AVERAGE_PAGES_READ_PER_MONTH = new Statistic("Average pages read per month", SINGLE_VALUE);
    static Statistic AVERAGE_BOOKS_READ_PER_MONTH = new Statistic("Average books read per month", SINGLE_VALUE);
    static Statistic AVERAGE_DAYS_TO_READ_A_BOOK = new Statistic("Average days to read a book", SINGLE_VALUE);
    static Statistic AVERAGE_RATING = new Statistic("Average rating", SINGLE_VALUE);

    //TODO: implement
    public static Statistic AUTHORS_WITH_MOST_FAVOURITES = new Statistic("Average rating", MAP);

    //TODO: days not reading books

}
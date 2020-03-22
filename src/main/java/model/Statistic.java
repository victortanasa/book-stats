package model;

public enum Statistic {

    MOST_READ_AUTHORS_BY_BOOK_COUNT("Most read authors by book count"),
    MOST_READ_AUTHORS_BY_PAGE_COUNT("Most read authors by page count"),
    MOST_READ_GENRES("Most read genres"),
    BOOKS_BY_DECADE("Books by decade"),
    BOOKS_BY_RATING("Books by rating"),
    AUTHORS_WITH_MOST_FAVOURITES("Authors with most favourites"),
    AVERAGE_RATING_FOR_AUTHORS("Average rating for authors"),
    AVERAGE_PAGE_NUMBER_FOR_AUTHORS("Average page number for authors");

    private String statistic;

    Statistic(final String statistic) {
        this.statistic = statistic;
    }

    public String getStringValue() {
        return statistic;
    }
}

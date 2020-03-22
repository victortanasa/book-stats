package model;

public enum Statistic {

    MOST_READ_AUTHORS_BY_BOOK_COUNT("Most read authors by book count"),
    MOST_READ_AUTHORS_BY_PAGE_COUNT("Most read authors by page count"),
    MOST_BOOKS_READ_BY_GENRE("Most books read by genre"),
    MOST_BOOKS_READ_BY_PUBLISHED_DECADE("Most books read by published decade"),
    TOTAL_RATINGS("Total Ratings"),
    AUTHORS_WITH_MOST_FAVOURITES("Authors with most favourites"),
    AVERAGE_RATING_FOR_AUTHORS("Average rating for authors"),
    AVERAGE_PAGE_NUMBER_FOR_AUTHORS("Average page number for authors"),
    BOOKS_READ_PER_MONTH("Books read per month"),
    PAGES_READ_PER_MONTH("Pages red per month");

    //TODO: average time to read a book, optionally for author
    //TODO: top shortest 10 books, top longest books
    //TODO: days not reading books
    //TODO: Average pages / books per month

    //TODO: most read authors per year - books and page number - sort by what

    private String statistic;

    Statistic(final String statistic) {
        this.statistic = statistic;
    }

    public String getStringValue() {
        return statistic;
    }
}

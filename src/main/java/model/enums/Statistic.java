package model.enums;

public enum Statistic {

    MOST_READ_AUTHORS_BY_BOOK_COUNT("Most read authors by book count"),
    MOST_READ_AUTHORS_BY_PAGE_COUNT("Most read authors by page count"),
    MOST_POPULAR_SHELVES("Most popular shelves"),
    MOST_BOOKS_READ_BY_PUBLISHED_DECADE("Most books read by published decade"),
    RATINGS_DISTRIBUTION("Ratings distribution"),
    AUTHORS_WITH_MOST_FAVOURITES("Authors with most favourites"),
    AVERAGE_RATING_FOR_AUTHORS("Average rating for authors"),
    AVERAGE_RATING("Average rating"),
    AVERAGE_PAGE_NUMBER_FOR_AUTHORS("Average page number for authors"),
    BOOKS_READ_PER_MONTH("Books read per month"),
    PAGES_READ_PER_MONTH("Pages red per month"),
    SHORTEST_BOOKS("Shortest Books"),
    LONGEST_BOOKS("Longest Books"),
    AVERAGE_DAYS_TO_READ_A_BOOK("Average days to read a book"),
    AVERAGE_DAYS_TO_READ_A_BOOK_PER_AUTHOR("Average days to read a book per author"),
    AVERAGE_PAGES_READ_PER_MONTH("Average pages read per month"),
    AVERAGE_BOOKS_READ_PER_MONTH("Average books read per month"),
    MOST_POPULAR_AUTHORS_BY_AVERAGE_NUMBER_OF_RATINGS("Most popular authors by average number of ratings");

    //TODO: number of books read by year
    //TODO: days not reading books
    //TODO: days (or average) between adding a book and reading it
    //TODO: pages read per month -> pages / days if month finished != month started

    //TODO: formats

    private String statistic;

    Statistic(final String statistic) {
        this.statistic = statistic;
    }

    public String getStringValue() {
        return statistic;
    }

}
package model;

public enum Statistic {

    MOST_READ_AUTHORS_BY_BOOKS_READ("Most read authors by books read"),
    MOST_READ_AUTHORS_BY_PAGES_READ("Most read authors by pages read"),
    MOST_READ_GENRES("Most read genres"),
    BOOKS_BY_DECADE("Books by decade");

    private String statistic;

    Statistic(final String statistic) {
        this.statistic = statistic;
    }

    public String getStringValue() {
        return statistic;
    }
}

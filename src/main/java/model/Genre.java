package model;

public enum Genre {

    SCI_FI("Sci-Fi"),
    SPACE_OPERA("Space Opera"),
    FANTASY("Fantasy"),
    SCIENCE("Science"),
    HISTORICAL_FICTION("Historical Fiction"),
    BIOGRAPHY("Biography");

    private String genre;

    Genre(final String genre) {
        this.genre = genre;
    }

    public String getStringValue() {
        return genre;
    }
}
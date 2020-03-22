package model;

import static java.time.temporal.ChronoUnit.DAYS;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;

public class Book {

    private static final String TO_STRING_FORMAT = "Title: %s\nAuthor: %s\nPage Number: %s\nRating: %s Stars\nRelease Year: %s\n" +
            "Date Started: %s\nDate Finished: %s\nGenre %s\nFavorite: %s";

    private String title;
    private String author;

    private Integer pageNumber;
    private Integer rating;
    private Integer releaseYear;

    private LocalDate dateStarted;
    private LocalDate dateFinished;

    private Genre genre;

    private boolean isFavorite;

    public Book(final String title, final String author, final Integer pageNumber, final Integer rating, final Integer releaseYear,
                final LocalDate dateStarted, final LocalDate dateFinished, final Genre genre, final boolean isFavorite) {
        this.title = title;
        this.author = author;

        this.pageNumber = pageNumber;
        this.rating = rating;
        this.releaseYear = releaseYear;

        this.dateStarted = dateStarted;
        this.dateFinished = dateFinished;

        this.genre = genre;

        this.isFavorite = isFavorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(final Integer rating) {
        this.rating = rating;
    }

    public LocalDate getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(final LocalDate dateStarted) {
        this.dateStarted = dateStarted;
    }

    public LocalDate getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(final LocalDate dateFinished) {
        this.dateFinished = dateFinished;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(final Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(final Genre genre) {
        this.genre = genre;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(final boolean favorite) {
        isFavorite = favorite;
    }

    public long getDaysReadIn() {
        final long difference = DAYS.between(dateStarted, dateFinished);
        return difference == 0 ? 1 : difference;
    }

    @Override
    public boolean equals(final Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT,
                this.title, this.author, this.pageNumber, this.rating, this.releaseYear, this.dateStarted,
                this.dateFinished, this.genre.getStringValue(), this.isFavorite ? "Yes" : "No");
    }

}
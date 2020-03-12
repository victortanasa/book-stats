package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

public class Book {

    private String title;
    private String author;

    private Long pageNumber;
    private Integer rating;
    private Integer releaseYear;

    private LocalDate dateStarted;
    private LocalDate dateFinished;

    private Genre genre;

    public Book(final String title, final String author, final Long pageNumber, final Integer rating,
                final LocalDate dateStarted, final LocalDate dateFinished, final Integer releaseYear,
                final Genre genre) {
        this.dateFinished = dateFinished;
        this.dateStarted = dateStarted;
        this.releaseYear = releaseYear;
        this.pageNumber = pageNumber;
        this.rating = rating;
        this.author = author;
        this.title = title;
        this.genre = genre;
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

    public Long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final Long pageNumber) {
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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
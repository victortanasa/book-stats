package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.List;

public class GoodReadsBook {

    private Long id;

    private String isbn;
    private String title;
    private String format;

    private Integer rating;
    private Integer readCount;
    private Integer pageNumber;
    private Integer ratingsCount;

    private Double averageRating;

    private LocalDate dateStarted;
    private LocalDate dateFinished;

    private Boolean owned;

    private List<String> authors;

    public GoodReadsBook(final Long id, final String isbn, final String title, final String format, final Integer rating, final Integer readCount,
                         final Integer pageNumber, final Integer ratingsCount, final Double averageRating, final LocalDate dateStarted,
                         final LocalDate dateFinished, final Boolean owned, final List<String> authors) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.format = format;
        this.rating = rating;
        this.readCount = readCount;
        this.pageNumber = pageNumber;
        this.ratingsCount = ratingsCount;
        this.averageRating = averageRating;
        this.dateStarted = dateStarted;
        this.dateFinished = dateFinished;
        this.owned = owned;
        this.authors = authors;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(final Integer rating) {
        this.rating = rating;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(final Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(final Integer ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(final Double averageRating) {
        this.averageRating = averageRating;
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

    public Boolean getOwned() {
        return owned;
    }

    public void setOwned(final Boolean owned) {
        this.owned = owned;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(final List<String> authors) {
        this.authors = authors;
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
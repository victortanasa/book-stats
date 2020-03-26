package model;

import static java.time.temporal.ChronoUnit.DAYS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.List;

public class GoodReadsBook {

    private static final String TO_STRING_SHORT_FORMAT = "Id: %s\nTitle: %s\nAuthors: %s";

    private Long id;

    private String isbn;
    private String isbn13;
    private String title;
    private String format;

    private Integer rating;
    private Integer readCount;
    private Integer pageNumber;
    private Integer ratingsCount;
    private Integer publicationYear;

    private Double averageRating;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateStarted;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateFinished;

    private Boolean owned;

    private List<String> authors;

    private List<String> shelves;

    public GoodReadsBook() {
    }

    public GoodReadsBook(final Long id, final String isbn, final String isbn13, final String title, final String format, final Integer rating, final Integer readCount,
                         final Integer pageNumber, final Integer ratingsCount, final Double averageRating, final LocalDate dateStarted,
                         final LocalDate dateFinished, final Boolean owned, final List<String> authors) {
        this.averageRating = averageRating;
        this.ratingsCount = ratingsCount;
        this.dateFinished = dateFinished;
        this.dateStarted = dateStarted;
        this.pageNumber = pageNumber;
        this.readCount = readCount;
        this.authors = authors;
        this.isbn13 = isbn13;
        this.format = format;
        this.rating = rating;
        this.title = title;
        this.owned = owned;
        this.isbn = isbn;
        this.id = id;
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

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(final String isbn13) {
        this.isbn13 = isbn13;
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

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(final Integer publicationYear) {
        this.publicationYear = publicationYear;
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

    public List<String> getShelves() {
        return shelves;
    }

    public void setShelves(final List<String> shelves) {
        this.shelves = shelves;
    }

    public GoodReadsBook withPublicationYear(final Integer publicationYear) {
        this.publicationYear = publicationYear;
        return this;
    }

    public GoodReadsBook withShelves(final List<String> shelves) {
        this.shelves = shelves;
        return this;
    }

    @JsonIgnore
    public GoodReadsBook getThis() {
        return this;
    }

    @JsonIgnore
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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public String toStringShort() {
        return String.format(TO_STRING_SHORT_FORMAT, id, title, authors);
    }
}
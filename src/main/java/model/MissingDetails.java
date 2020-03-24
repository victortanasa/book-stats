package model;

import java.util.List;

public class MissingDetails {

    private Integer publicationYear;

    private List<String> shelves;

    public MissingDetails(final Integer publicationYear, final List<String> shelves) {
        this.publicationYear = publicationYear;
        this.shelves = shelves;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public List<String> getShelves() {
        return shelves;
    }
}
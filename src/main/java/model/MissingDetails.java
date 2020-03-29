package model;

import java.util.List;

public class MissingDetails {

    private Integer publicationYear;

    private List<Shelve> shelves;

    public MissingDetails(final Integer publicationYear, final List<Shelve> shelves) {
        this.publicationYear = publicationYear;
        this.shelves = shelves;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public List<Shelve> getShelves() {
        return shelves;
    }
}
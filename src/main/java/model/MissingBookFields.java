package model;

import java.util.List;

public class MissingBookFields {

    private final static String TO_STRING_FORMAT = "Id: %s\nTitle: %s\nAuthors: %s\nMissing Fields: %s";

    private GoodReadsBook book;

    private List<BookField> missingFields;

    public MissingBookFields(final GoodReadsBook book, final List<BookField> missingFields) {
        this.missingFields = missingFields;
        this.book = book;
    }

    public GoodReadsBook getBook() {
        return book;
    }

    public List<BookField> getMissingFields() {
        return missingFields;
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT, book.getId(), book.getTitle(), book.getAuthors(), missingFields);
    }
}
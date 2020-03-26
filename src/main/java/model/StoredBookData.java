package model;

import java.util.Map;

public class StoredBookData {

    private Long bookId;

    private Map<BookField, Object> fieldValues;

    public StoredBookData() {
    }

    public StoredBookData(final Long bookId, final Map<BookField, Object> fieldValues) {
        this.fieldValues = fieldValues;
        this.bookId = bookId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(final Long bookId) {
        this.bookId = bookId;
    }

    public Map<BookField, Object> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(final Map<BookField, Object> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public Object getFieldValue(final BookField bookField) {
        return fieldValues.get(bookField);
    }
}
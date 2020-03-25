package model;

import java.util.Map;

public class StoredBookData {

    private Long id;

    private Map<BookField, Object> fieldValueMap;

    public StoredBookData() {
    }

    public StoredBookData(Long id, Map<BookField, Object> fieldValueMap) {
        this.id = id;
        this.fieldValueMap = fieldValueMap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<BookField, Object> getFieldValueMap() {
        return fieldValueMap;
    }

    public void setFieldValueMap(Map<BookField, Object> fieldValueMap) {
        this.fieldValueMap = fieldValueMap;
    }

    public Object getFieldValue(final BookField bookField) {
        return fieldValueMap.get(bookField);
    }
}
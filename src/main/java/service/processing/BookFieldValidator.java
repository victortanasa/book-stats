package service.processing;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.google.common.base.Function;
import model.Book;
import model.enums.BookField;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookFieldValidator {

    private static final Map<BookField, Function<Book, ?>> FIELD_GETTERS;

    static {
        FIELD_GETTERS = newHashMap();
//        FIELD_GETTERS.put(BookField.ISNB, Book::getIsbn);
//        FIELD_GETTERS.put(BookField.ISBN_13, Book::getIsbn13);

        FIELD_GETTERS.put(BookField.OWNED, Book::getOwned);
        FIELD_GETTERS.put(BookField.TITLE, Book::getTitle);
        FIELD_GETTERS.put(BookField.FORMAT, Book::getFormat);
        FIELD_GETTERS.put(BookField.RATING, Book::getRating);
        FIELD_GETTERS.put(BookField.AUTHORS, Book::getAuthors);
        FIELD_GETTERS.put(BookField.SHELVES, Book::getShelves);
        FIELD_GETTERS.put(BookField.READ_COUNT, Book::getReadCount);
        FIELD_GETTERS.put(BookField.PAGE_NUMBER, Book::getPageNumber);
        FIELD_GETTERS.put(BookField.DATE_STARTED, Book::getDateStarted);
        FIELD_GETTERS.put(BookField.DATE_FINISHED, Book::getDateFinished);
        FIELD_GETTERS.put(BookField.RATINGS_COUNT, Book::getRatingsCount);
        FIELD_GETTERS.put(BookField.AVERAGE_RATING, Book::getAverageRating);
        FIELD_GETTERS.put(BookField.PUBLICATION_YEAR, Book::getPublicationYear);
    }

    public Map<Book, List<BookField>> getMissingFields(final List<Book> books) {
        return books.stream()
                .collect(toMap(Book::getThis, this::getFieldsWithNoValue));
    }

    private List<BookField> getFieldsWithNoValue(final Book book) {
        return FIELD_GETTERS.entrySet().stream()
                .filter(getter -> Objects.isNull(getter.getValue().apply(book)))
                .map(Map.Entry::getKey)
                .collect(toList());
    }

}
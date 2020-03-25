package service;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

import com.google.common.base.Function;
import model.BookField;
import model.GoodReadsBook;
import model.MissingDataResult;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookValidator {

    private static final Map<BookField, Function<GoodReadsBook, ?>> FIELD_GETTERS;

    static {
        FIELD_GETTERS = newHashMap();
//        FIELD_GETTERS.put(BookField.ISNB, GoodReadsBook::getIsbn);
//        FIELD_GETTERS.put(BookField.ISBN_13, GoodReadsBook::getIsbn13);

        FIELD_GETTERS.put(BookField.OWNED, GoodReadsBook::getOwned);
        FIELD_GETTERS.put(BookField.TITLE, GoodReadsBook::getTitle);
        FIELD_GETTERS.put(BookField.FORMAT, GoodReadsBook::getFormat);
        FIELD_GETTERS.put(BookField.RATING, GoodReadsBook::getRating);
        FIELD_GETTERS.put(BookField.AUTHORS, GoodReadsBook::getAuthors);
        FIELD_GETTERS.put(BookField.SHELVES, GoodReadsBook::getShelves);
        FIELD_GETTERS.put(BookField.READ_COUNT, GoodReadsBook::getReadCount);
        FIELD_GETTERS.put(BookField.PAGE_NUMBER, GoodReadsBook::getPageNumber);
        FIELD_GETTERS.put(BookField.DATE_STARTED, GoodReadsBook::getDateStarted);
        FIELD_GETTERS.put(BookField.DATE_FINISHED, GoodReadsBook::getDateFinished);
        FIELD_GETTERS.put(BookField.RATINGS_COUNT, GoodReadsBook::getRatingsCount);
        FIELD_GETTERS.put(BookField.AVERAGE_RATING, GoodReadsBook::getAverageRating);
        FIELD_GETTERS.put(BookField.PUBLICATION_YEAR, GoodReadsBook::getPublicationYear);
    }

    public List<MissingDataResult> getMissingData(final List<GoodReadsBook> books) {
        return books.stream()
                .map(book -> new MissingDataResult(book, getFieldsWithNoValue(book)))
                .filter(missingData -> !missingData.getMissingFields().isEmpty())
                .collect(toList());
    }

    private List<BookField> getFieldsWithNoValue(final GoodReadsBook book) {
        return FIELD_GETTERS.entrySet().stream()
                .filter(getter -> Objects.isNull(getter.getValue().apply(book)))
                .map(Map.Entry::getKey)
                .collect(toList());
    }

}
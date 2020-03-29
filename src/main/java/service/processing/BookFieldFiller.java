package service.processing;

import static com.google.common.collect.Maps.newHashMap;
import static utils.TransformationUtils.getDate;
import static utils.TransformationUtils.getInteger;

import model.Book;
import model.StoredBookData;
import model.enums.BookField;
import service.StorageService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class BookFieldFiller {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Map<BookField, BiConsumer<Book, Object>> FIELD_SETTERS;

    static {
        FIELD_SETTERS = newHashMap();
        FIELD_SETTERS.put(BookField.ISNB, (goodReadsBook, isbn) -> goodReadsBook.setIsbn(isbn.toString()));
        FIELD_SETTERS.put(BookField.FORMAT, (goodReadsBook, format) -> goodReadsBook.setFormat(format.toString()));
        FIELD_SETTERS.put(BookField.PAGE_NUMBER, (goodReadsBook, pageNumber) -> goodReadsBook.setPageNumber(getInteger(pageNumber.toString())));
        FIELD_SETTERS.put(BookField.DATE_STARTED, (goodReadsBook, dateStarted) -> goodReadsBook.setDateStarted(getDate(DATE_FORMATTER, dateStarted.toString())));
        FIELD_SETTERS.put(BookField.DATE_FINISHED, (goodReadsBook, dateFinished) -> goodReadsBook.setDateFinished(getDate(DATE_FORMATTER, dateFinished.toString())));
    }

    private static final StorageService STORAGE_SERVICE = new StorageService();

    public List<Book> fillMissingFieldsForBooks(final String userId, final Map<Book, List<BookField>> books) {
        return books.entrySet().stream()
                .map(entry -> fillMissingBookFields(userId, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Book fillMissingBookFields(final String userId, final Book book, final List<BookField> missingFields) {
        final Optional<StoredBookData> storedDataForBook = getStoredDataForBook(userId, book);

        storedDataForBook.ifPresent(data -> fillMissingFields(book, storedDataForBook.get(), missingFields));

        return book;
    }

    private Optional<StoredBookData> getStoredDataForBook(final String userId, final Book book) {
        return STORAGE_SERVICE.loadStoredBookData(userId).stream()
                .filter(data -> data.getBookId().equals(book.getId()))
                .findFirst();
    }

    private void fillMissingFields(final Book book, final StoredBookData storedBookData, final List<BookField> missingFields) {
        missingFields.forEach(field -> {
            final Object fieldValue = storedBookData.getFieldValue(field);
            if (!Objects.isNull(fieldValue)) {
                FIELD_SETTERS.get(field).accept(book, fieldValue);
            }
        });
    }

}
package service.processing;

import static com.google.common.collect.Maps.newHashMap;
import static utils.TransformationUtils.getDate;
import static utils.TransformationUtils.getInteger;

import model.Book;
import model.BookField;
import model.StoredBookData;
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

    private List<StoredBookData> STORED_DATA;

    public BookFieldFiller() {
        STORED_DATA = new StorageService().loadStoredBookData();
    }

    public List<Book> fillMissingFieldsForBooks(final Map<Book, List<BookField>> books) {
        return books.entrySet().stream()
                .map(entry -> fillMissingBookFields(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Book fillMissingBookFields(final Book book, final List<BookField> missingFields) {
        final Optional<StoredBookData> storedDataForBook = getStoredDataForBook(book);

        storedDataForBook.ifPresent(data -> fillMissingFields(book, missingFields, storedDataForBook.get()));

        return book;
    }

    private void fillMissingFields(final Book book, final List<BookField> missingFields, final StoredBookData storedBookData) {
        missingFields.forEach(field -> {
            final Object fieldValue = storedBookData.getFieldValue(field);
            if (!Objects.isNull(fieldValue)) {
                FIELD_SETTERS.get(field).accept(book, fieldValue);
            }
        });
    }

    private Optional<StoredBookData> getStoredDataForBook(final Book book) {
        return STORED_DATA.stream()
                .filter(data -> data.getBookId().equals(book.getId()))
                .findFirst();
    }

}
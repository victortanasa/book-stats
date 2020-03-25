package service.processing;

import static com.google.common.collect.Maps.newHashMap;
import static utils.TransformationUtils.getDate;
import static utils.TransformationUtils.getInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.BookField;
import model.GoodReadsBook;
import model.StoredBookData;
import utils.BookLoader;
import utils.PrinterUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class BookFieldFiller {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String COULD_NOT_LOAD_STORED_DATA_MESSAGE = "Could not load stored data! Exception was: %s";
    private static final String STORED_DATA_FILE = "missingData.json";

    private static final List<StoredBookData> STORED_DATA;
    private static final Map<BookField, BiConsumer<GoodReadsBook, Object>> FIELD_SETTERS;

    static {
        STORED_DATA = loadStoredData();

        FIELD_SETTERS = newHashMap();
        FIELD_SETTERS.put(BookField.ISNB, (goodReadsBook, isbn) -> goodReadsBook.setIsbn(isbn.toString()));
        FIELD_SETTERS.put(BookField.FORMAT, (goodReadsBook, format) -> goodReadsBook.setFormat(format.toString()));
        FIELD_SETTERS.put(BookField.PAGE_NUMBER, (goodReadsBook, pageNumber) -> goodReadsBook.setPageNumber(getInteger(pageNumber.toString())));
        FIELD_SETTERS.put(BookField.DATE_STARTED, (goodReadsBook, dateStarted) -> goodReadsBook.setDateStarted(getDate(DATE_FORMATTER, dateStarted.toString())));
        FIELD_SETTERS.put(BookField.DATE_FINISHED, (goodReadsBook, dateFinished) -> goodReadsBook.setDateFinished(getDate(DATE_FORMATTER, dateFinished.toString())));
    }

    //TODO: naming?
    public BookFieldFiller() {
        BookLoader.getFileContent(STORED_DATA_FILE);
    }

    public List<GoodReadsBook> fillMissingFieldsForBooks(final Map<GoodReadsBook, List<BookField>> books) {
        return books.entrySet().stream()
                .map(entry -> fillMissingBookFields(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private GoodReadsBook fillMissingBookFields(final GoodReadsBook book, final List<BookField> missingFields) {
        final Optional<StoredBookData> storedDataForBook = getStoredDataForBook(book);

        storedDataForBook.ifPresent(data -> fillMissingFields(book, missingFields, storedDataForBook.get()));

        return book;
    }

    private void fillMissingFields(final GoodReadsBook book, final List<BookField> missingFields, final StoredBookData storedBookData) {
        missingFields.forEach(field -> {
            final Object fieldValue = storedBookData.getFieldValue(field);
            if (!Objects.isNull(fieldValue)) {
                FIELD_SETTERS.get(field).accept(book, fieldValue);
            }
        });
    }

    private Optional<StoredBookData> getStoredDataForBook(final GoodReadsBook book) {
        return STORED_DATA.stream()
                .filter(data -> data.getId().equals(book.getId()))
                .findFirst();
    }

    private static List<StoredBookData> loadStoredData() {
        try {
            final String missingDataJson = BookLoader.getFileContent(STORED_DATA_FILE);
            return OBJECT_MAPPER.readValue(missingDataJson, new TypeReference<List<StoredBookData>>() {
            });
        } catch (final IOException e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_LOAD_STORED_DATA_MESSAGE, e));
            return null;
        }
    }

}
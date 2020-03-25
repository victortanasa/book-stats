package service;

import static com.google.common.collect.Maps.newHashMap;
import static utils.TransformationUtils.getDate;
import static utils.TransformationUtils.getInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.BookField;
import model.GoodReadsBook;
import model.MissingDataResult;
import model.StoredBookData;
import utils.BookLoader;
import utils.PrinterUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class MissingBookDataFiller {

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

    //TODO: 1) pretty
    //TODO: 2) NAMING -  missingDataResults not so ok
    //TODO: 3) move classes to correct packages
    public MissingBookDataFiller() {
        BookLoader.getFileContent(STORED_DATA_FILE);
    }

    public List<GoodReadsBook> fillMissingBooksData(final List<MissingDataResult> missingDataResults) {
        return missingDataResults.stream()
                .map(this::fillMissingBookData)
                .collect(Collectors.toList());
    }

    private GoodReadsBook fillMissingBookData(final MissingDataResult missingDataResult) {
        final StoredBookData storedDataForBook = getStoredDataForBook(missingDataResult.getBook());

        if (!Objects.isNull(storedDataForBook)) {
            missingDataResult.getMissingFields().forEach(field -> {
                final Object fieldValue = storedDataForBook.getFieldValue(field);
                if (!Objects.isNull(fieldValue)) {
                    FIELD_SETTERS.get(field).accept(missingDataResult.getBook(), fieldValue);
                }
            });
        }

        return missingDataResult.getBook();
    }

    private StoredBookData getStoredDataForBook(final GoodReadsBook book) {
        return STORED_DATA.stream()
                .filter(data -> data.getId().equals(book.getId()))
                .findFirst()
                .orElse(null);
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
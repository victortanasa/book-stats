package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.GoodReadsBook;
import model.StoredBookData;
import utils.PrinterUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class StorageService {

    private static final String COULD_NOT_LOAD_STORED_DATA_MESSAGE = "Could not load stored book data. Exception was: %s";
    private static final String COULD_NOT_SAVE_BOOKS_MESSAGE = "Could not save books. Exception was: %s";
    private static final String COULD_NOT_LOAD_BOOKS_MESSAGE = "Could not load books. Exception was: %s";

    private static final String STORAGE_LOCATION = "src/main/resources/storage/%s";
    private static final String STORED_DATA_FILE = "storedBookData.json";
    private static final String BOOKS_FILE = "books.json";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    void saveBooks(final List<GoodReadsBook> books) {
        try {
            final String booksJson = OBJECT_MAPPER.writeValueAsString(books);
            Files.write(Paths.get(String.format(STORAGE_LOCATION, BOOKS_FILE)), booksJson.getBytes());
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_SAVE_BOOKS_MESSAGE, e));
        }
    }

    List<GoodReadsBook> loadBooks() {
        try {
            final String booksJson = new String(Files.readAllBytes(Paths.get(String.format(STORAGE_LOCATION, BOOKS_FILE))));
            return OBJECT_MAPPER.readValue(booksJson, new TypeReference<List<GoodReadsBook>>() {
            });
        } catch (IOException e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_LOAD_BOOKS_MESSAGE, e));
            return null;
        }
    }

    public List<StoredBookData> loadStoredBookData() {
        try {
            final String missingDataJson = new String(Files.readAllBytes(Paths.get(String.format(STORAGE_LOCATION, STORED_DATA_FILE))));
            return OBJECT_MAPPER.readValue(missingDataJson, new TypeReference<List<StoredBookData>>() {
            });
        } catch (final IOException e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_LOAD_STORED_DATA_MESSAGE, e));
            return null;
        }
    }
}
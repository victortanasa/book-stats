package service;

import static com.google.common.collect.Lists.newArrayList;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Book;
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

    private static final String STORAGE_LOCATION = "src/main/resources/storage/";
    private static final String STORED_DATA_FILE = "storedBookData-%s.json";
    private static final String BOOKS_FILE = "books-%s.json";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    void saveBooks(final String userId, final List<Book> books) {
        try {
            final String booksJson = OBJECT_MAPPER.writeValueAsString(books);
            Files.write(Paths.get(STORAGE_LOCATION + String.format(BOOKS_FILE, userId)), booksJson.getBytes(), CREATE, TRUNCATE_EXISTING);
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_SAVE_BOOKS_MESSAGE, e));
        }
    }

    List<Book> loadBooks(final String userId) {
        try {
            final String booksJson = new String(Files.readAllBytes(Paths.get(STORAGE_LOCATION + String.format(BOOKS_FILE, userId))));
            return OBJECT_MAPPER.readValue(booksJson, new TypeReference<List<Book>>() {
            });
        } catch (IOException e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_LOAD_BOOKS_MESSAGE, e));
            return null;
        }
    }

    public List<StoredBookData> loadStoredBookData(final String userId) {
        try {
            final String missingDataJson = new String(Files.readAllBytes(Paths.get(STORAGE_LOCATION + String.format(STORED_DATA_FILE, userId))));
            return OBJECT_MAPPER.readValue(missingDataJson, new TypeReference<List<StoredBookData>>() {
            });
        } catch (final IOException e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_LOAD_STORED_DATA_MESSAGE, e));
            return newArrayList();
        }
    }
}
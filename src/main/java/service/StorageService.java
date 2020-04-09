package service;

import static com.google.common.collect.Lists.newArrayList;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Book;
import model.ShelveMapping;
import model.StoredBookData;
import model.enums.BookField;
import model.enums.ShelveName;
import org.apache.commons.lang3.tuple.Pair;
import utils.PrinterUtils;
import utils.TransformationUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class StorageService {

    private static final String COULD_NOT_LOAD_STORED_DATA_MESSAGE = "Could not load stored book data. Exception was: %s";
    private static final String COULD_NOT_LOAD_SHELVE_MAPPINGS = "Could not load shelve mappings. Exception was: %s";
    private static final String COULD_NOT_LOAD_PROPERTIES_MESSAGE = "Could not load properties. Exception was: %s";
    private static final String COULD_NOT_SAVE_BOOKS_MESSAGE = "Could not save books. Exception was: %s";
    private static final String COULD_NOT_LOAD_BOOKS_MESSAGE = "Could not load books. Exception was: %s";

    private static final String RESOURCES_LOCATION = "src/main/resources/";
    private static final String STORAGE_LOCATION = RESOURCES_LOCATION + "storage/";

    private static final String BOOK_STATS_PROPERTIES_FILE = "bookStats.properties";
    private static final String STORED_BOOK_DATA_FILE = "storedBookData-%s.json";
    private static final String SHELVE_MAPPING_FILE = "shelveMappings.json";
    private static final String LIBRARY_LOCATION = STORAGE_LOCATION + "library/";
    private static final String BOOKS_FILE = LIBRARY_LOCATION + "/books-%s-%s.json";

    private static final String SHELVES_TO_EXCLUDE_THAT_START_WITH = "shelvesToExcludeThatStartWith";
    private static final String BOOK_FIELDS_TO_OVERRIDE = "bookFieldsToOverride";
    private static final String SHELVES_TO_EXCLUDE = "shelvesToExclude";

    private static final String EQUALS = "=";
    private static final String COMMA = ",";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Properties PROPERTIES = new Properties();

    public StorageService() {
        loadProperties();
    }

    void saveBooks(final String userId, final ShelveName shelveName, final List<Book> books) {
        try {
            final String booksJson = OBJECT_MAPPER.writeValueAsString(books);
            Files.write(Paths.get(String.format(BOOKS_FILE, userId, shelveName.getValue())), booksJson.getBytes(), CREATE, TRUNCATE_EXISTING);
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_SAVE_BOOKS_MESSAGE, e));
        }
    }

    List<Book> loadBooks(final String userId, final ShelveName shelveName) {
        try {
            final String booksJson = new String(Files.readAllBytes(Paths.get(String.format(BOOKS_FILE, userId, shelveName.getValue()))));
            return OBJECT_MAPPER.readValue(booksJson, new TypeReference<List<Book>>() {
            });
        } catch (IOException e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_LOAD_BOOKS_MESSAGE, e));
            return newArrayList();
        }
    }

    public List<StoredBookData> loadStoredBookData(final String userId) {
        try {
            return OBJECT_MAPPER.readValue(readFileContent(String.format(STORED_BOOK_DATA_FILE, userId)),
                    new TypeReference<List<StoredBookData>>() {
                    });
        } catch (final IOException e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_LOAD_STORED_DATA_MESSAGE, e));
            return newArrayList();
        }
    }

    public List<ShelveMapping> loadShelveMappings() {
        try {
            return OBJECT_MAPPER.readValue(readFileContent(SHELVE_MAPPING_FILE),
                    new TypeReference<List<ShelveMapping>>() {
                    });
        } catch (final IOException e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_LOAD_SHELVE_MAPPINGS, e));
            return newArrayList();
        }
    }

    public List<String> getShelvesToExclude() {
        return newArrayList(PROPERTIES.getProperty(SHELVES_TO_EXCLUDE).split(COMMA));
    }

    public List<String> getShelvesToExcludeThatStartWith() {
        return newArrayList(PROPERTIES.getProperty(SHELVES_TO_EXCLUDE_THAT_START_WITH).split(COMMA));
    }

    public Map<Long, BookField> getBooksFieldsToOverride() {
        final String property = PROPERTIES.getProperty(BOOK_FIELDS_TO_OVERRIDE);
        return newArrayList(property.split(COMMA)).stream()
                .map(this::toBookIdBookFieldPair)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private Pair<Long, BookField> toBookIdBookFieldPair(final String string) {
        final Long bookId = TransformationUtils.getLong(string.split(EQUALS)[0]);
        final BookField bookField = BookField.valueOf(string.split(EQUALS)[1]);

        return Pair.of(bookId, bookField);
    }

    private static String readFileContent(final String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(STORAGE_LOCATION + fileName)));
    }

    private static void loadProperties() {
        try {
            PROPERTIES.load(new FileReader(RESOURCES_LOCATION + BOOK_STATS_PROPERTIES_FILE));
        } catch (final IOException e) {
            final String message = String.format(COULD_NOT_LOAD_PROPERTIES_MESSAGE, e);
            PrinterUtils.printSimple(message);
            throw new IllegalStateException(message);
        }
    }

}
package service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Book;
import model.ShelveMapping;
import model.Statistic;
import model.StoredBookData;
import model.enums.ShelveName;
import utils.PrinterUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

public class StorageService {

    private static final String COULD_NOT_LOAD_STORED_DATA_MESSAGE = "Could not load stored book data. Exception was: %s";
    private static final String COULD_NOT_LOAD_SHELVE_MAPPINGS = "Could not load shelve mappings. Exception was: %s";
    private static final String COULD_NOT_LOAD_PROPERTIES_MESSAGE = "Could not load properties. Exception was: %s";
    private static final String COULD_NOT_SAVE_BOOKS_MESSAGE = "Could not save books. Exception was: %s";
    private static final String COULD_NOT_LOAD_BOOKS_MESSAGE = "Could not load books. Exception was: %s";

    private static final String CSV_FILE_NAME_FORMAT = "%s.csv";
    private static final String STATISTIC_FORMAT = "%s,%s";

    private static final String RESOURCES_LOCATION = "src/main/resources/";
    private static final String STORAGE_LOCATION = RESOURCES_LOCATION + "storage/";
    private static final String LIBRARY_LOCATION = STORAGE_LOCATION + "library/";
    private static final String CSV_LOCATION = RESOURCES_LOCATION + "csv/";

    private static final String BOOK_STATS_PROPERTIES_FILE = "bookStats.properties";
    private static final String STORED_BOOK_DATA_FILE = "storedBookData-%s.json";
    private static final String SHELVE_MAPPING_FILE = "shelveMappings.json";
    private static final String BOOKS_FILE = "books-%s-%s.json";

    private static final String SHELVES_TO_EXCLUDE_THAT_START_WITH = "shelvesToExcludeThatStartWith";
    private static final String SHELVES_TO_EXCLUDE = "shelvesToExclude";
    private static final String COMMA = ",";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Properties PROPERTIES = new Properties();

    public StorageService() {
        loadProperties();
    }

    void saveBooks(final String userId, final ShelveName shelveName, final List<Book> books) {
        try {
            final String booksJson = OBJECT_MAPPER.writeValueAsString(books);
            Files.write(Paths.get(LIBRARY_LOCATION + String.format(BOOKS_FILE, userId, shelveName.getValue())), booksJson.getBytes(), CREATE, TRUNCATE_EXISTING);
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_SAVE_BOOKS_MESSAGE, e));
        }
    }

    List<Book> loadBooks(final String userId, final ShelveName shelveName) {
        try {
            final String booksJson = new String(Files.readAllBytes(Paths.get(LIBRARY_LOCATION + String.format(BOOKS_FILE, userId, shelveName.getValue()))));
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

    public void saveStatisticToCsv(final Statistic statistic, final Map<String, ? extends Comparable<?>> map) {
        final List<String> csv = getCsvLines(map);

        addLabels(csv, statistic.getStatisticKeyName(), statistic.getStatisticValueName());

        writeStatistic(statistic.getFileName(), csv);
    }

    public void saveAndMergeStatisticToCsv(final Statistic firstStatistic, final Statistic secondStatistic,
                                           final Map<String, ?> firstMap, final Map<String, ?> secondMap) {
        final Map<String, String> resultMap = newLinkedHashMap();

        Stream.of(secondMap, firstMap)
                .flatMap(m -> m.entrySet().stream())
                .forEach(entry -> mergeValues(resultMap, entry));

        final List<String> csv = getCsvLines(resultMap);
        addLabels(csv, firstStatistic.getStatisticKeyName(), secondStatistic.getStatisticValueName(), firstStatistic.getStatisticValueName());

        writeStatistic(firstStatistic.getFileName() + secondStatistic.getFileName(), csv);
    }

    private void writeStatistic(final String fileName, final List<String> csvLines) {
        try {
            Files.write(Paths.get(CSV_LOCATION + String.format(CSV_FILE_NAME_FORMAT, fileName)), csvLines, CREATE, TRUNCATE_EXISTING);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void mergeValues(final Map<String, String> resultMap, final Map.Entry<String, ?> entry) {
        final Object value = resultMap.get(entry.getKey());
        if (Objects.isNull(value)) {
            resultMap.put(entry.getKey(), entry.getValue().toString());
        } else {
            resultMap.put(entry.getKey(), String.format(STATISTIC_FORMAT, value, entry.getValue()));
        }
    }

    private List<String> getCsvLines(final Map<String, ?> map) {
        return map.entrySet().stream()
                .map(entry -> String.format(STATISTIC_FORMAT, entry.getKey(), entry.getValue()))
                .limit(15)
                .collect(toList());
    }

    private void addLabels(final List<String> csv, final String... labels) {
        csv.add(0, String.join(",", labels));
    }

    private String readFileContent(final String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(STORAGE_LOCATION + fileName)));
    }

    private void loadProperties() {
        try {
            PROPERTIES.load(new FileReader(RESOURCES_LOCATION + BOOK_STATS_PROPERTIES_FILE));
        } catch (final IOException e) {
            final String message = String.format(COULD_NOT_LOAD_PROPERTIES_MESSAGE, e);
            PrinterUtils.printSimple(message);
            throw new IllegalStateException(message);
        }
    }

}
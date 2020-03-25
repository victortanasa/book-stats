package utils;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import model.Book;
import model.Genre;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class BookLoader {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String COULD_NOT_LOAD_BOOKS_MESSAGE = "Could not load books! Exception is: %s";
    private static final String BOOKS_LOCATION = "src/main/resources/books.csv";
    private static final String COMMA = ",";
    private static final String SEARCH_XML = "src/main/resources/responses/bookSearch.xml";
    private static final String SHELVE_XML = "src/main/resources/responses/readShelve.xml";

    public static Set<Book> loadBooksFromFile() {
        return getBooksFromFile().stream()
                .map(BookLoader::toBook)
                .collect(toSet());
    }

    public static String getFileContent(final String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/main/resources/" + fileName)));
        } catch (final Exception e) {
            PrinterUtils.printSimple(e.toString());
            return null;
        }
    }

    private static Book toBook(final String fileLine) {
        final String[] split = fileLine.split(COMMA);

        return new Book(split[0], //title
                split[1], //author
                Integer.valueOf(split[2]), //page number
                Integer.valueOf(split[3]), //rating
                Integer.valueOf(split[4]), //release year
                LocalDate.parse(split[5], DATE_FORMATTER), //date started
                LocalDate.parse(split[6], DATE_FORMATTER), //date finished
                Genre.valueOf(split[7]), //genre
                Boolean.valueOf(split[8])); //is favorite
    }

    private static List<String> getBooksFromFile() {
        try {
            return Files.readAllLines(Paths.get(BOOKS_LOCATION)).stream()
                    .skip(1)
                    .collect(toList());
        } catch (final IOException e) {
            final String message = String.format(COULD_NOT_LOAD_BOOKS_MESSAGE, e);
            PrinterUtils.printSimple(message);
            throw new IllegalStateException(message);
        }
    }

}
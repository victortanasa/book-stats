import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import model.Book;
import model.Genre;
import model.Printer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

class BookLoader {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String COULD_NOT_LOAD_BOOKS_MESSAGE = "Could not load books! Exception is: %s";
    private static final String BOOKS_LOCATION = "src/main/resources/books.csv";
    private static final String COMMA = ",";

    static Set<Book> loadBooksFromFile() {
        return getBookLinesFromFile().stream()
                .map(BookLoader::toBook)
                .collect(toSet());
    }

    private static Book toBook(final String fileLine) {
        final String[] split = fileLine.split(COMMA);

        return new Book(split[0],
                split[1],
                Long.valueOf(split[2]),
                Integer.valueOf(split[3]),
                Integer.valueOf(split[4]),
                LocalDate.parse(split[5], DATE_FORMATTER),
                LocalDate.parse(split[6], DATE_FORMATTER),
                Genre.valueOf(split[7]),
                Boolean.valueOf(split[8]));
    }

    private static List<String> getBookLinesFromFile() {
        try {
            return Files.readAllLines(Paths.get(BOOKS_LOCATION)).stream()
                    .skip(1)
                    .collect(toList());
        } catch (final IOException e) {
            final String message = String.format(COULD_NOT_LOAD_BOOKS_MESSAGE, e);
            Printer.simplePrint(message);
            throw new IllegalStateException(message);
        }
    }

}
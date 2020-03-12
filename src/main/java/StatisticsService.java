import model.Book;
import model.Genre;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class StatisticsService {

    private static final String DECADE_FORMAT = "%d's";

    private static final Set<Book> LIBRARY;

    static {
        LIBRARY = BookLoader.loadBooks();
    }

    static Map<String, Long> getMostReadAuthorsByBooksRead() {
        return sortDescendingByValue(LIBRARY.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting())));
    }

    static Map<String, Long> getMostReadAuthorsByPagesRead() {
        return sortDescendingByValue(LIBRARY.stream().collect(
                Collectors.groupingBy(Book::getAuthor, Collectors.summingLong(Book::getPageNumber))));
    }

    static Map<Genre, Long> getMostReadGenres() {
        return sortDescendingByValue(LIBRARY.stream()
                .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting())));
    }

    static Map<String, Long> getReadBooksByDecade() {
        return sortDescendingByValue(LIBRARY.stream()
                .collect(Collectors.groupingBy(book -> getDecade(book.getReleaseYear()), Collectors.counting())));
    }

    private static <T> Map<T, Long> sortDescendingByValue(final Map<T, Long> map) {
        return map.entrySet()
                .stream()
                .sorted((Map.Entry.<T, Long>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private static String getDecade(final Integer releaseYear) {
        return String.format(DECADE_FORMAT, releaseYear % 100 - releaseYear % 10);
    }

}
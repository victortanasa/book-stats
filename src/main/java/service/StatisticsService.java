package service;

import static java.util.Map.Entry.comparingByValue;

import model.Book;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StatisticsService {

    private static final String DECADE_FORMAT = "%d's";

    private Set<Book> library;

    //TODO: pages per month
    //TODO: most read authors per year - books and page number
    public StatisticsService(final Set<Book> library) {
        this.library = library;
    }

    public Map<String, Long> getMostReadAuthorsByBookCount() {
        return sortDescendingByValue(library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting())));
    }

    public Map<String, Long> getMostReadAuthorsByPageCount() {
        return sortDescendingByValue(library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.summingLong(Book::getPageNumber))));
    }

    public Map<String, Long> getBooksByDecade() {
        return sortDescendingByValue(library.stream()
                .collect(Collectors.groupingBy(book -> getDecade(book.getReleaseYear()), Collectors.counting())));
    }

    public Map<String, Long> getBooksByRating() {
        return sortDescendingByValue(library.stream()
                .collect(Collectors.groupingBy(book -> book.getRating().toString(), Collectors.counting())));
    }

    public Map<String, Long> getMostReadGenres() {
        return sortDescendingByValue(library.stream()
                .collect(Collectors.groupingBy(book -> book.getGenre().getStringValue(), Collectors.counting())));
    }

    public Map<String, Double> getAverageRatingForAuthors() {
        return sortDescendingByValue(library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.averagingInt(Book::getRating))));
    }

    public Map<String, Double> getAveragePageNumberForAuthors() {
        return sortDescendingByValue(library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.averagingInt(Book::getPageNumber))));
    }

    public Map<String, Long> getAuthorsWithMostFavourites() {
        return sortDescendingByValue(library.stream()
                .filter(Book::isFavorite)
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting())));
    }

    private static <T, V extends Comparable<V>> Map<T, V> sortDescendingByValue(final Map<T, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private static String getDecade(final Integer releaseYear) {
        return String.format(DECADE_FORMAT, releaseYear - releaseYear % 10);
    }

}
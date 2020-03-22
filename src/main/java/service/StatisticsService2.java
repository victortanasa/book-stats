package service;

import static java.util.Map.Entry.comparingByValue;

import model.Book;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StatisticsService2 {

    private static final String DECADE_FORMAT = "%d's";

    private Set<Book> library;

    public StatisticsService2(final Set<Book> library) {
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

    public Map<String, Long> getBooksByGenre() {
        return sortDescendingByValue(library.stream()
                .collect(Collectors.groupingBy(book -> book.getGenre().getStringValue(), Collectors.counting())));
    }

    public Map<String, Double> getAverageRatingForAuthors() {
        return sortDescendingByValue(library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.averagingInt(Book::getRating))));
    }

    public Map<String, Long> getAuthorsWithMostFavourites() {
        return sortDescendingByValue(library.stream()
                .filter(Book::isFavorite)
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting())));
    }

    private static <T extends Comparable<T>> Map<String, T> sortDescendingByValue(final Map<String, T> map) {
        return map.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private static String getDecade(final Integer releaseYear) {
        return String.format(DECADE_FORMAT, releaseYear % 100 - releaseYear % 10);
    }

}
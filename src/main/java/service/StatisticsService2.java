package service;

import model.Book;

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
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
    }

    public Map<String, Long> getMostReadAuthorsByPageCount() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.summingLong(Book::getPageNumber)));
    }

    public Map<String, Long> getBooksByDecade() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> getDecade(book.getReleaseYear()), Collectors.counting()));
    }

    public Map<String, Long> getBooksByRating() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> book.getRating().toString(), Collectors.counting()));
    }

    public Map<String, Long> getBooksByGenre() {
        return library.stream()
                .collect(Collectors.groupingBy(book -> book.getGenre().getStringValue(), Collectors.counting()));
    }

    public Map<String, Double> getAverageRatingForAuthors() {
        return library.stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.averagingInt(Book::getRating)));
    }

    public Map<String, Long> getAuthorsWithMostFavourites() {
        return library.stream()
                .filter(Book::isFavorite)
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
    }

    private static String getDecade(final Integer releaseYear) {
        return String.format(DECADE_FORMAT, releaseYear % 100 - releaseYear % 10);
    }

}
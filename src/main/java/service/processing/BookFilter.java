package service.processing;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

import model.Book;

import java.util.List;

public class BookFilter {

    private static final List<Long> EXCLUDED_BOOK_IDS = newArrayList(382549L, 7669L, 143618L, 7670L);

    public List<Book> filterUnwantedBooks(final List<Book> books) {
        return books.stream()
                .filter(book -> !EXCLUDED_BOOK_IDS.contains(book.getId()))
                .collect(toList());
    }
}
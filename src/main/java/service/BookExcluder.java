package service;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

import model.GoodReadsBook;

import java.util.List;

public class BookExcluder {

    private static final List<Long> EXCLUDED_BOOK_IDS = newArrayList(382549L, 7669L, 143618L, 7670L);

    public static List<GoodReadsBook> removeExcludedBooks(final List<GoodReadsBook> books) {
        return books.stream()
                .filter(book -> !EXCLUDED_BOOK_IDS.contains(book.getId()))
                .collect(toList());
    }
}
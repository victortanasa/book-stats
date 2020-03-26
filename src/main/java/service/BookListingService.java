package service;

import static com.google.common.collect.Maps.newHashMap;
import static model.Statistic.LONGEST_BOOKS;
import static model.Statistic.SHORTEST_BOOKS;

import com.google.common.base.Function;
import model.GoodReadsBook;
import model.Statistic;
import model.sorting.SortOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookListingService {

    private Map<Statistic, Function<Integer, List<String>>> bookListings;

    private List<GoodReadsBook> library;

    public BookListingService(final List<GoodReadsBook> library) {
        this.library = library;

        bookListings = newHashMap();

        bookListings.put(SHORTEST_BOOKS, this::getShortestBooks);
        bookListings.put(LONGEST_BOOKS, this::getLongestBooks);
    }

    //TODO: not string, but book;
    //TODO: move SHORTEST_BOOKS out of statistics
    public List<String> getBookListing(final Statistic statistic, final int limit) {
        return bookListings.get(statistic).apply(limit);
    }

    private List<String> getShortestBooks(final int limit) {
        return getBooksByLength(library, SortOrder.DESC, limit);
    }

    private List<String> getLongestBooks(final int limit) {
        return getBooksByLength(library, SortOrder.ASC, limit);
    }

    private static List<String> getBooksByLength(final List<GoodReadsBook> library, final SortOrder sortOrder, final int limit) {
        final Comparator<GoodReadsBook> comparator = SortOrder.ASC.equals(sortOrder) ?
                Comparator.comparing(GoodReadsBook::getPageNumber).reversed() : Comparator.comparing(GoodReadsBook::getPageNumber);

        return library.stream().
                sorted(comparator)
                .limit(limit)
                .map(GoodReadsBook::toStringShort)
                .collect(Collectors.toList());
    }
}
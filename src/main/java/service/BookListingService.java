package service;

import static com.google.common.collect.Maps.newHashMap;

import com.google.common.base.Function;
import model.Book;
import model.Statistic;
import model.enums.sort.SortOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookListingService {

//    private Map<Statistic, Function<Integer, List<String>>> bookListings;
//
//    private List<Book> library;
//
//    public BookListingService(final List<Book> library) {
//        this.library = library;
//
//        bookListings = newHashMap();
//
//        bookListings.put(SHORTEST_BOOKS, this::getShortestBooks);
//        bookListings.put(LONGEST_BOOKS, this::getLongestBooks);
//    }
//
//    //TODO: not string, but book;
//    //TODO: move SHORTEST_BOOKS out of statistics
//    //TODO: most popular books (by ratings num)
//    public List<String> getBookListing(final StatisticName statisticName, final int limit) {
//        return bookListings.get(statisticName).apply(limit);
//    }
//
//    private List<String> getShortestBooks(final int limit) {
//        return getBooksByLength(library, SortOrder.DESC, limit);
//    }
//
//    private List<String> getLongestBooks(final int limit) {
//        return getBooksByLength(library, SortOrder.ASC, limit);
//    }
//
//    private static List<String> getBooksByLength(final List<Book> library, final SortOrder sortOrder, final int limit) {
//        final Comparator<Book> comparator = SortOrder.ASC.equals(sortOrder) ?
//                Comparator.comparing(Book::getPageNumber).reversed() : Comparator.comparing(Book::getPageNumber);
//
//        return library.stream().
//                sorted(comparator)
//                .limit(limit)
//                .map(Book::toStringShort)
//                .collect(Collectors.toList());
//    }
}
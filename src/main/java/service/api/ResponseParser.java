package service.api;

import static java.util.stream.Collectors.toList;
import static utils.TransformationUtils.*;

import model.Book;
import model.MissingDetails;
import model.Shelve;
import model.enums.ShelveName;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringEscapeUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import utils.PrinterUtils;
import utils.TransformationUtils;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ResponseParser {

    private static final String COULD_NOT_BUILD_RESPONSE_MESSAGE = "Could not build document from response  %s. Exception was: %s";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy").withLocale(Locale.US);

    private static final int SHELF_LIMIT = 100;

    private static final SAXBuilder SAX_BUILDER = new SAXBuilder();

    List<Book> getBooks(final String response) {
        final Document document = buildDocument(response);

        final List<Element> books = document.getRootElement()
                .getChild("reviews")
                .getChildren("review");

        return books.stream()
                .map(this::toBook)
                .collect(toList());
    }

    Map<ShelveName, Integer> getBookCountToRetrievePerShelf(final String response) {
        final Document document = buildDocument(response);

        final List<Element> userShelves = document.getRootElement()
                .getChild("shelves")
                .getChildren("user_shelf");

        return Stream.of(ShelveName.values())
                .map(shelve -> Pair.of(shelve, getBookCountForShelve(shelve.getValue(), userShelves)))
                .filter(pair -> Objects.nonNull(pair.getValue()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private Integer getBookCountForShelve(final String shelveName, final List<Element> userShelves) {
        return userShelves.stream()
                .filter(shelve -> shelveName.equals(shelve.getChild("name").getValue()))
                .map(shelve -> shelve.getChild("book_count").getValue())
                .findFirst()
                .map(TransformationUtils::getInteger)
                .orElse(null);
    }

    MissingDetails getMissingDetails(final String response) {
        final Document document = buildDocument(response);

        final String publicationYear = document.getRootElement()
                .getChild("book")
                .getChild("work")
                .getChild("original_publication_year")
                .getValue();

        final List<Element> allShelves = document.getRootElement()
                .getChild("book")
                .getChild("popular_shelves")
                .getChildren("shelf");

        final List<Shelve> popularShelves = allShelves.stream()
                .map(this::toUserShelve)
                .limit(SHELF_LIMIT)
                .collect(toList());

        return new MissingDetails(getInteger(publicationYear), popularShelves);
    }

    private Document buildDocument(final String response) {
        try {
            return SAX_BUILDER.build(new ByteArrayInputStream(response.getBytes()));
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_BUILD_RESPONSE_MESSAGE, response, e.getMessage()));
            throw new IllegalStateException(String.format(COULD_NOT_BUILD_RESPONSE_MESSAGE, response, e.getMessage()), e);
        }
    }

    private Shelve toUserShelve(final Element shelve) {
        final String name = shelve.getAttribute("name").getValue();
        final Integer popularity = getInteger(shelve.getAttribute("count").getValue());
        return new Shelve(name, popularity);
    }

    private Book toBook(final Element element) {
        final Element book = element.getChild("book");

        final String id = book.getChild("id").getValue();
        final String isbn = book.getChild("isbn").getValue();
        final String isbn13 = book.getChild("isbn13").getValue();
        final String format = book.getChild("format").getValue();
        final String pageNumber = book.getChild("num_pages").getValue();
        final String title = StringEscapeUtils.escapeCsv(book.getChild("title_without_series").getValue());
        final String ratingsCount = book.getChild("ratings_count").getValue();
        final String averageRating = book.getChild("average_rating").getValue();

        final String owned = element.getChild("owned").getValue();
        final String rating = element.getChild("rating").getValue();
        final String dateFinished = element.getChild("read_at").getValue();
        final String readCount = element.getChild("read_count").getValue();
        final String dateStarted = element.getChild("started_at").getValue();

        final List<String> authors = book
                .getChild("authors")
                .getChildren("author")
                .stream()
                .map(author -> author.getChild("name").getValue())
                .collect(toList());

        return new Book(Long.valueOf(id),
                getNullIfBlank(isbn),
                getNullIfBlank(isbn13),
                getNullIfBlank(title),
                getNullIfBlank(format),
                getInteger(rating),
                getInteger(readCount),
                getInteger(pageNumber),
                getInteger(ratingsCount),
                getDouble(averageRating),
                getDate(DATE_FORMATTER, dateStarted),
                getDate(DATE_FORMATTER, dateFinished),
                "1".equals(owned),
                authors);
    }

}
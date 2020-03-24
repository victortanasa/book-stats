package service;

import static java.util.stream.Collectors.toList;

import model.GoodReadsBook;
import model.MissingDetails;
import model.Shelve;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import utils.PrinterUtils;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;

class GoodReadsDeserializer {

    private static final String COULD_NOT_BUILD_RESPONSE_MESSAGE = "Could not build document from response  %s. Exception was: %s";

    private static final int SHELF_LIMIT = 100;

    private static final SAXBuilder SAX_BUILDER = new SAXBuilder();

    List<GoodReadsBook> getBooksFromStringResponse(final String response) {
        final Document document = buildDocument(response);

        final List<Element> elements = document.getRootElement()
                .getChild("reviews")
                .getChildren("review");

        return elements.stream()
                .map(this::toBook)
                .collect(toList());
    }

    Integer getNumberOfBooksRead(final String response) {
        final Document document = buildDocument(response);

        final List<Element> shelves = document.getRootElement()
                .getChild("shelves")
                .getChildren("user_shelf");

        final String bookCount = shelves.stream()
                .filter(shelve -> "read".equals(shelve.getChild("name").getValue()))
                .map(shelve -> shelve.getChild("book_count").getValue())
                .findFirst()
                .orElse(null);

        return !Objects.isNull(bookCount) ? getInteger(bookCount) : null;
    }

    MissingDetails getMissingBookDetails(final String response) {
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
                .map(this::toShelve)
                .limit(SHELF_LIMIT)
                .collect(toList());

        return new MissingDetails(getInteger(publicationYear), ShelveAnalyzer.getTopShelves(popularShelves));
    }

    private Document buildDocument(final String response) {
        try {
            return SAX_BUILDER.build(new ByteArrayInputStream(response.getBytes()));
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_BUILD_RESPONSE_MESSAGE, response, e.getMessage()));
            throw new IllegalStateException("");
        }
    }

    private Shelve toShelve(final Element shelve) {
        final String name = shelve.getAttribute("name").getValue();
        final Integer popularity = getInteger(shelve.getAttribute("count").getValue());
        return new Shelve(name, popularity);
    }

    private GoodReadsBook toBook(final Element element) {
        final Element book = element.getChild("book");

        final String id = book.getChild("id").getValue();
        final String isbn = book.getChild("isbn").getValue();
        final String isbn13 = book.getChild("isbn13").getValue();
        final String format = book.getChild("format").getValue();
        final String pageNumber = book.getChild("num_pages").getValue();
        final String title = book.getChild("title_without_series").getValue();
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

        return new GoodReadsBook(Long.valueOf(id),
                getNullIfBlank(isbn),
                getNullIfBlank(isbn13),
                getNullIfBlank(title),
                getNullIfBlank(format),
                getInteger(rating),
                getInteger(readCount),
                getInteger(pageNumber),
                getInteger(ratingsCount),
                getDouble(averageRating),
                null, //TODO: dateStarted
                null, //TODO: dateFinished
                "1".equals(owned),
                authors);
    }

    private static String getNullIfBlank(final String value) {
        return StringUtils.isBlank(value) ? null : value;
    }

    private static Integer getInteger(final String value) {
        return !StringUtils.isBlank(value) ? Integer.parseInt(value) : null;
    }

    private static Long getLong(final String value) {
        return !StringUtils.isBlank(value) ? Long.parseLong(value) : null;
    }

    private static Double getDouble(final String value) {
        return !StringUtils.isBlank(value) ? Double.parseDouble(value) : null;
    }
}
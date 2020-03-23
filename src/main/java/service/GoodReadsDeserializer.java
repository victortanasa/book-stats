package service;

import static java.util.stream.Collectors.toList;

import model.GoodReadsBook;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import utils.PrinterUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

class GoodReadsDeserializer {

    private static final String COULD_NOT_GET_ORIGINAL_RELEASE_YEAR_FROM_API_MESSAGE = "Could not get original release year for response %s\n. Exception was: %s";
    private static final String COULD_NOT_GET_NUMBER_OF_BOOKS_READ_FROM_API_MESSAGE = "Could not get number of books read from response %s\n.  Exception was: %s";
    private static final String COULD_NOT_GET_BOOKS_FROM_API_MESSAGE = "Could not get book from response %s\n. Exception was: %s";

    private static final SAXBuilder SAX_BUILDER = new SAXBuilder();

    Integer getPublicationYear(final String response, final Long expectedBookId) {
        try {
            final Document document = SAX_BUILDER.build(new ByteArrayInputStream(response.getBytes()));

            final List<Element> foundBooks = document.getRootElement().getChild("search").getChild("results").getChildren("work");

            return foundBooks.stream().
                    filter(book -> expectedBookId.equals(getLong(book.getChild("best_book").getChild("id").getValue())))
                    .map(book -> getInteger(book.getChild("original_publication_year").getValue()))
                    .findFirst()
                    .orElse(-1);
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_GET_ORIGINAL_RELEASE_YEAR_FROM_API_MESSAGE, response, e.getMessage()));
            return null;
        }
    }

    Integer getNumberOfBookRead(final String response) {
        try {
            final Document document = SAX_BUILDER.build(new ByteArrayInputStream(response.getBytes()));

            final List<Element> shelves = document.getRootElement().getChild("shelves").getChildren("user_shelf");
            final String bookCount = shelves.stream()
                    .filter(shelve -> "read".equals(shelve.getChild("name").getValue()))
                    .map(shelve -> shelve.getChild("book_count").getValue())
                    .findFirst().orElse("-456");

            return getInteger(bookCount);
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_GET_NUMBER_OF_BOOKS_READ_FROM_API_MESSAGE, response, e.getMessage()));
            return null;
        }
    }

    List<GoodReadsBook> getBooksFromStringResponse(final String response) {
        try {
            final Document document = SAX_BUILDER.build(new ByteArrayInputStream(response.getBytes()));
            final List<Element> elements = document.getRootElement().getChild("reviews").getChildren("review");

            return elements.stream()
                    .map(this::toBook)
                    .collect(toList());
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_GET_BOOKS_FROM_API_MESSAGE, response, e.getMessage()));
            return null;
        }
    }

    private GoodReadsBook toBook(final Element element) {
        final Element book = element.getChild("book");

        final String id = book.getChild("id").getValue();
        final String isbn = element.getChild("book").getChild("isbn").getValue();
        final String format = element.getChild("book").getChild("format").getValue();
        final String pageNumber = element.getChild("book").getChild("num_pages").getValue();
        final String title = element.getChild("book").getChild("title_without_series").getValue();
        final String ratingsCount = element.getChild("book").getChild("ratings_count").getValue();
        final String averageRating = element.getChild("book").getChild("average_rating").getValue();

        final String owned = element.getChild("owned").getValue();
        final String rating = element.getChild("rating").getValue();
        final String dateFinished = element.getChild("read_at").getValue();
        final String readCount = element.getChild("read_count").getValue();
        final String dateStarted = element.getChild("started_at").getValue();

        final List<String> authors = book.getChild("authors").getChildren("author").stream()
                .map(author -> author.getChild("name").getValue())
                .collect(toList());

        return new GoodReadsBook(Long.valueOf(id),
                isbn,
                title,
                format,
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

    private static Integer getInteger(final String value) {
        return !StringUtils.isBlank(value) ? Integer.parseInt(value) : -123;
    }

    private static Long getLong(final String value) {
        return !StringUtils.isBlank(value) ? Long.parseLong(value) : -123;
    }

    private static Double getDouble(final String value) {
        return !StringUtils.isBlank(value) ? Double.parseDouble(value) : -123;
    }
}
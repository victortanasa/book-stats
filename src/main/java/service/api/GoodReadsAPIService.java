package service.api;

import static java.util.stream.Collectors.toList;

import model.Book;
import model.MissingDetails;
import model.Shelve;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import utils.PrinterUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class GoodReadsAPIService {

    private static final String COULD_NOT_LOAD_BOOKS_ERROR_MESSAGE = "Could not load books, cannot continue.";
    private static final String INTERRUPTED_MESSAGE = "Something interrupted!";

    private static final String API_KEY = "aB9VcY1rOGCzxMONqjk8Ug";

    private static final int BOOK_LIMIT_PER_CALL = 100;
    private static final int DEFAULT_TIMEOUT_IN_SECONDS = 10;

    private static final String GOOD_READS_BASE_URL = "https://www.goodreads.com/";

    private static final String GET_READ_BOOKS_URL = "review/list?v=2&key=%s&id=%s&shelf=%s&page=%s&per_page=100";
    private static final String GET_READ_SHELF_URL = "shelf/list.xml?key=%s&user_id=%s&page=1";
    private static final String GET_BOOK_DETAILS_URL = "book/show/%s.xml?key=%s";

    private final ResponseParser responseParser;
    private final WebClient webClient;

    public GoodReadsAPIService() {
        final ExchangeStrategies exchangeStrategies = ExchangeStrategies
                .builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1000)).build();

        webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(GOOD_READS_BASE_URL)
                .build();

        responseParser = new ResponseParser();
    }

    public List<Shelve> getNumberOfBooksToRetrieve(final String userId) {
        final String response = doRequest(String.format(GET_READ_SHELF_URL, API_KEY, userId));

        return responseParser.getNumberOfBooksToRetrieve(response);
    }

    public List<Book> getBooksForShelve(final String userId, final Shelve shelve) {
        return IntStream.rangeClosed(1, getNumberOfCalls(shelve.getPopularity()))
                .mapToObj(pageNumber -> getBooks(userId, shelve.getName(), pageNumber))
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<Book> getBooks(final String userId, final String shelve, int pageNumber) {
        final String response = doRequest(String.format(GET_READ_BOOKS_URL, API_KEY, userId, shelve, pageNumber));

        return responseParser.getBooks(response);
    }

    public MissingDetails getMissingBookDetails(final Long bookId) {
        final String response = doRequest(String.format(GET_BOOK_DETAILS_URL, bookId, API_KEY));

        return responseParser.getMissingDetails(response);
    }

    private String doRequest(final String endpoint) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (final InterruptedException e) {
            PrinterUtils.printSimple(INTERRUPTED_MESSAGE + e.getMessage());
            throw new IllegalStateException(COULD_NOT_LOAD_BOOKS_ERROR_MESSAGE);
        }

        final ClientResponse clientResponse = webClient.get()
                .uri(endpoint)
                .exchange()
                .timeout(Duration.ofSeconds(DEFAULT_TIMEOUT_IN_SECONDS)).block();

        if (Objects.isNull(clientResponse)) {
            throw new IllegalStateException(COULD_NOT_LOAD_BOOKS_ERROR_MESSAGE);
        }

        return clientResponse.bodyToMono(String.class).block();
    }

    //TODO: pretty
    private static Integer getNumberOfCalls(final Integer numberOfBooks) {
        return !Objects.isNull(numberOfBooks) ? (int) Math.ceil(new Double(numberOfBooks) / BOOK_LIMIT_PER_CALL) : 0;
    }

}
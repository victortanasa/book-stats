package service;

import model.GoodReadsBook;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import utils.PrinterUtils;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GoodReadsRequestService {

    private static final String COULD_NOT_LOAD_BOOKS_ERROR_MESSAGE = "Could not load books, cannot continue.";
    private static final String INTERRUPTED_MESSAGE = "Something interrupted!";

    private static final int DEFAULT_TIMEOUT_IN_SECONDS = 10;

    private static final String GOOD_READS_BASE_URL = "https://www.goodreads.com/";

    private static final String SEARCH_BOOK_URL = "search/index.xml?q=%s&key=aB9VcY1rOGCzxMONqjk8Ug";
    //TODO: extract token
    private static final String GET_READ_SHELF_URL = "shelf/list.xml?key=aB9VcY1rOGCzxMONqjk8Ug&user_id=60626198&page=1";

    private static final String GET_READ_BOOKS_URL = "review/list?v=2&key=aB9VcY1rOGCzxMONqjk8Ug&id=60626198&shelf=read&page=1&per_page=%s";

    private WebClient webClient;
    private GoodReadsDeserializer goodReadsDeserializer;

    public GoodReadsRequestService() {
        final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1000)).build();

        webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(GOOD_READS_BASE_URL)
                .build();

        goodReadsDeserializer = new GoodReadsDeserializer();
    }

    public Integer getPublicationYear(final String searchTerm, final Long bookId) {
        final String response = doRequest(String.format(SEARCH_BOOK_URL, searchTerm));

        return goodReadsDeserializer.getPublicationYear(response, bookId);
    }

    public Integer getNumberOfBooksRead() {
        final String response = doRequest(GET_READ_SHELF_URL);

        return goodReadsDeserializer.getNumberOfBookRead(response);
    }

    public List<GoodReadsBook> getAllBooksRead(final Integer numberOfBooksToRetrieve) {
        final String response = doRequest(String.format(GET_READ_BOOKS_URL, numberOfBooksToRetrieve));

        return goodReadsDeserializer.getBooksFromStringResponse(response);
    }

    private String doRequest(final String endpoint) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (final InterruptedException e) {
            PrinterUtils.printSimple(INTERRUPTED_MESSAGE + e.getMessage());
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

}
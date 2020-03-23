package service;

import model.GoodReadsBook;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class GoodReadsRequestService {

    private static final String COULD_NOT_LOAD_BOOKS_ERROR_MESSAGE = "Could not load books, cannot continue.";
    private static final int DEFAULT_TIMEOUT_IN_SECONDS = 10;

    private static final String GOOD_READS_BASE_URL = "https://www.goodreads.com/";

    private static final String SEARCH_BOOK_URL = "search/index.xml?q=1857987632&key=aB9VcY1rOGCzxMONqjk8Ug";
    //TODO: extract token
    private static final String GET_READ_SHELF_URL = "shelf/list.xml?key=aB9VcY1rOGCzxMONqjk8Ug&user_id=60626198&page=1";

    //TODO: page, leave 1, but per page -> value from read shelves
    private static final String GET_READ_BOOKS_URL = "review/list?v=2&key=aB9VcY1rOGCzxMONqjk8Ug&id=60626198&shelf=read&page=1&per_page=%s";
//    private static final String GET_READ_BOOKS_URL = "review/list?v=2&key=aB9VcY1rOGCzxMONqjk8Ug&id=60626198&shelf=read&page=1,200&per_page=200";

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

    public void getBookInfo() {
        final String response = webClient.get()
                .uri(SEARCH_BOOK_URL)
                .exchange()
                .timeout(Duration.ofSeconds(DEFAULT_TIMEOUT_IN_SECONDS)).block().bodyToMono(String.class).block();

        System.out.println(response);
    }

    public Integer getNumberOfBooksRead() {
        //TODO: one method
        final String response = webClient.get()
                .uri(GET_READ_SHELF_URL)
                .exchange()
                .timeout(Duration.ofSeconds(DEFAULT_TIMEOUT_IN_SECONDS)).block().bodyToMono(String.class).block();

        return goodReadsDeserializer.getNumberOfBookRead(response);
    }

    public List<GoodReadsBook> getAllBooksRead(final Integer numberOfBooksToRetrieve) {
        final ClientResponse clientResponse = webClient.get()
                .uri(String.format(GET_READ_BOOKS_URL, numberOfBooksToRetrieve))
                .exchange()
                .timeout(Duration.ofSeconds(DEFAULT_TIMEOUT_IN_SECONDS)).block();

        if (Objects.isNull(clientResponse)) {
            throw new IllegalStateException(COULD_NOT_LOAD_BOOKS_ERROR_MESSAGE);
        }

        final String response = clientResponse.bodyToMono(String.class).block();

        return goodReadsDeserializer.getBooksFromStringResponse(response);
    }

}
package service;

import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

public class GoodReadsRequestService {

    private static final String GOOD_READS_BASE_URL = "https://www.goodreads.com/";

    private static final String SEARCH_BOOK_URL = "search/index.xml?q=1857987632&key=aB9VcY1rOGCzxMONqjk8Ug";
    private static final String GET_READ_SHELF_URL = "review/list?v=2&key=aB9VcY1rOGCzxMONqjk8Ug&id=60626198&shelf=read";

    private WebClient webClient;

    public GoodReadsRequestService() {
        webClient = WebClient.create(GOOD_READS_BASE_URL);
    }

    public void getBookInfo() {
        final String response = webClient.get()
                .uri(SEARCH_BOOK_URL)
                .exchange()
                .timeout(Duration.ofSeconds(15)).block().bodyToMono(String.class).block();

        System.out.println(response);
    }

    public void getReadShelve() {
        final String response = webClient.get()
                .uri(GET_READ_SHELF_URL)
                .exchange()
                .timeout(Duration.ofSeconds(15)).block().bodyToMono(String.class).block();

        System.out.println(response);
    }

}
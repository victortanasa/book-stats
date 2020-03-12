import model.Genre;

import java.util.Map;

public class BookStats {

    public static void main(final String[] args) {
        final Map<String, Long> mostReadAuthors = StatisticsService.getMostReadAuthorsByBooksRead();

        mostReadAuthors.entrySet().forEach(System.out::println);

        System.out.println("-----------------------------");

        final Map<String, Long> mostReadAuthorsByPagesRead = StatisticsService.getMostReadAuthorsByPagesRead();

        mostReadAuthorsByPagesRead.entrySet().forEach(System.out::println);

        System.out.println("-----------------------------");

        final Map<Genre, Long> mostReadGenres = StatisticsService.getMostReadGenres();

        mostReadGenres.entrySet().forEach(System.out::println);

        System.out.println("-----------------------------");

        final Map<String, Long> readBooksByDecade = StatisticsService.getReadBooksByDecade();

        readBooksByDecade.entrySet().forEach(System.out::println);

    }
}
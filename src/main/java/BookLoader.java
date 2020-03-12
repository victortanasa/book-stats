import static com.google.common.collect.Sets.newHashSet;

import model.Book;
import model.Genre;

import java.time.LocalDate;
import java.util.Set;

class BookLoader {

    static Set<Book> loadBooks() {
        return newHashSet(new Book("The Sirens of Titan", "Kurt Vonnegut", 330L, 5, getDate(2018, 6, 7), getDate(2018, 6, 10), 1956, Genre.SCI_FI),
                new Book("Mother Night", "Kurt Vonnegut", 336L, 4, getDate(2018, 8, 6), getDate(2018, 8, 12), 1957, Genre.HISTORICAL_FICTION),
                new Book("The Dispossesed", "Ursula K. Le Guin", 350L, 5, getDate(2018, 7, 1), getDate(2018, 7, 10), 1972, Genre.SCI_FI),
                new Book("Consider Phlebas", "Ian M. Banks", 520L, 5, getDate(2019, 10, 5), getDate(2019, 10, 11), 1989, Genre.SCI_FI));
    }

    private static LocalDate getDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

}
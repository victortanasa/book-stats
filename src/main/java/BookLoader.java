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
                new Book("Consider Phlebas", "Ian M. Banks", 520L, 5, getDate(2019, 10, 5), getDate(2019, 10, 11), 1989, Genre.SCI_FI),
                new Book("The Player of Games", "Ian M. Banks", 320L, 5, getDate(2019, 10, 12), getDate(2019, 10, 14), 1991, Genre.SCI_FI),
                new Book("Use of Weapons", "Ian M. Banks", 440L, 4, getDate(2019, 10, 17), getDate(2019, 10, 22), 1993, Genre.SCI_FI),
                new Book("The Martian", "Andy Weir", 350L, 3, getDate(2020, 1, 2), getDate(2020, 1, 5), 1993, Genre.SCI_FI));
    }

    private static LocalDate getDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

}
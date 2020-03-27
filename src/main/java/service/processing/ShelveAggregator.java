package service.processing;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

import model.ShelveMapping;
import model.UserShelve;
import service.StorageService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShelveAggregator {

    //TODO: Docs
    //TODO: maybe top category, sub-category
    //TODO: initialize list of authors from get all Books step and compose authors shelves; maybe series name? (foundation)

    private static final List<String> SHELVE_TO_EXCLUDE = newArrayList("to-read", "currently-reading", "owned", "default", "favorites",
            "books-i-own", "ebook", "kindle", "library", "audiobook", "owned-books", "audiobooks", "my-books", "ebooks", "to-buy",
            "english", "calibre", "books", "british", "audio", "my-library", "favourites", "re-read", "general", "e-books", "audio-book",
            "audio-books", "audible", "book-club", "series", "sf-masterworks", "abandoned", "dnf", "the-expanse", "expanse", "asimov", "fiction", "non-fiction",
            "nonfiction", "philip-k-dick", "culture", "isaac-asimov", "vonnegut", "peter-f-hamilton", "alastair-reynolds", "foundation",
            "revelation-space");

    private static final int SHELVE_LIMIT = 3;

    private static final List<String> SHELVE_NAMES_STARTS_WITH_FILTER = newArrayList("read-", "hugo", "nebula");

    private static final StorageService STORAGE_SERVICE = new StorageService();
    private List<ShelveMapping> shelveMappings;

    public ShelveAggregator() {
        shelveMappings = STORAGE_SERVICE.loadShelveMappings();
    }

    public List<String> getTopShelves(final List<UserShelve> allShelves) {
        final List<UserShelve> normalizedAndFilteredShelves = allShelves.stream()
                .filter(this::shelveNotExcluded)
                .filter(this::shelveNotInStartsWithFilterList)
                .map(this::normalizeShelveName)
                .collect(toList());

        final Map<String, Integer> shelvesPopularityMap = normalizedAndFilteredShelves.stream()
                .collect(Collectors.groupingBy(UserShelve::getName, Collectors.summingInt(UserShelve::getPopularity)));

        return shelvesPopularityMap.entrySet().stream()
                .sorted(comparing(Map.Entry::getValue, reverseOrder()))
                .limit(SHELVE_LIMIT)
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    private UserShelve normalizeShelveName(final UserShelve userShelve) {
        shelveMappings.stream()
                .filter(mapping -> mapping.getAlternateValues().contains(userShelve.getName()))
                .findFirst()
                .ifPresent(mapping -> userShelve.setName(mapping.getNormalizedValue()));

        return userShelve;
    }

    private boolean shelveNotExcluded(final UserShelve userShelve) {
        return !SHELVE_TO_EXCLUDE.contains(userShelve.getName());
    }

    private boolean shelveNotInStartsWithFilterList(final UserShelve userShelve) {
        return SHELVE_NAMES_STARTS_WITH_FILTER.stream()
                .noneMatch(shelveNameToFilter -> userShelve.getName().startsWith(shelveNameToFilter));
    }

}
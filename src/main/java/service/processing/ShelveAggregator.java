package service.processing;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

import model.UserShelve;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShelveAggregator {

    //TODO: move to file
    //TODO: maybe top category, sub-category

    //TODO: Docs
    //TODO: add more shelve mappings
    //TODO: initialize list of authors from get all Books step and compose authors shelves; maybe series name? (foundation)

    private static final List<String> SHELVE_NAMES_FILTER = newArrayList("to-read", "currently-reading", "owned", "default", "favorites",
            "books-i-own", "ebook", "kindle", "library", "audiobook", "owned-books", "audiobooks", "my-books", "ebooks", "to-buy",
            "english", "calibre", "books", "british", "audio", "my-library", "favourites", "re-read", "general", "e-books", "audio-book",
            "audio-books", "audible", "book-club", "series", "sf-masterworks", "abandoned", "dnf", "the-expanse", "expanse", "asimov", "fiction", "non-fiction",
            "nonfiction", "philip-k-dick", "culture", "isaac-asimov", "vonnegut", "peter-f-hamilton", "alastair-reynolds", "foundation",
            "revelation-space");

    private static final int SHELVE_LIMIT = 3;

    private static final List<String> SHELVE_NAMES_STARTS_WITH_FILTER;
    private static final List<ShelveMapping> SHELVE_NAME_MAPPINGS;

    static {
        SHELVE_NAME_MAPPINGS = newArrayList();
        SHELVE_NAME_MAPPINGS.add(new ShelveMapping("sci-fi", newArrayList("science-fiction", "scifi", "sf", "sci-fi", "space")));
        SHELVE_NAME_MAPPINGS.add(new ShelveMapping("mil-sci-fi", newArrayList("military-sci-fi", "military-science-fiction", "mil-sci-fi", "military", "war")));
        SHELVE_NAME_MAPPINGS.add(new ShelveMapping("sci-fi-fantasy", newArrayList("sci-fi-fantasy", "scifi-fantasy")));
        SHELVE_NAME_MAPPINGS.add(new ShelveMapping("dystopian", newArrayList("dystopian", "dystopia")));
        SHELVE_NAME_MAPPINGS.add(new ShelveMapping("classics", newArrayList("classic", "classics")));

        SHELVE_NAMES_STARTS_WITH_FILTER = newArrayList("read-in", "read-", "hugo", "nebula");
    }

    public static List<String> getTopShelves(final List<UserShelve> allShelves) {
        final List<UserShelve> normalizedAndFilteredShelves = allShelves.stream()
                .filter(ShelveAggregator::shelveNotInFilterList)
                .filter(ShelveAggregator::shelveNotInStartsWithFilterList)
                .map(ShelveAggregator::normalizeShelveName)
                .collect(toList());

        final Map<String, Integer> shelvesPopularityMap = normalizedAndFilteredShelves.stream()
                .collect(Collectors.groupingBy(UserShelve::getName, Collectors.summingInt(UserShelve::getPopularity)));

        return shelvesPopularityMap.entrySet().stream()
                .sorted(comparing(Map.Entry::getValue, reverseOrder()))
                .limit(SHELVE_LIMIT)
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    private static UserShelve normalizeShelveName(final UserShelve userShelve) {
        SHELVE_NAME_MAPPINGS.stream()
                .filter(mapping -> mapping.getAlternateValues().contains(userShelve.getName()))
                .findFirst()
                .ifPresent(mapping -> userShelve.setName(mapping.getNormalizedValue()));

        return userShelve;
    }

    private static boolean shelveNotInFilterList(final UserShelve userShelve) {
        return !SHELVE_NAMES_FILTER.contains(userShelve.getName());
    }

    private static boolean shelveNotInStartsWithFilterList(final UserShelve userShelve) {
        return SHELVE_NAMES_STARTS_WITH_FILTER.stream()
                .noneMatch(shelveNameToFilter -> userShelve.getName().startsWith(shelveNameToFilter));
    }

    static class ShelveMapping {

        private String normalizedValue;

        private List<String> alternateValues;

        ShelveMapping(final String normalizedValue, final List<String> alternateValues) {
            this.normalizedValue = normalizedValue;
            this.alternateValues = alternateValues;
        }

        String getNormalizedValue() {
            return normalizedValue;
        }

        List<String> getAlternateValues() {
            return alternateValues;
        }
    }

}
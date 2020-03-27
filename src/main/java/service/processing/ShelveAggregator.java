package service.processing;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

import model.Shelve;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShelveAggregator {

    //TODO: move to file
    //TODO: maybe top category, sub-category

    //TODO: Docs
    //TODO: add more shelve mappings
    //TODO: initialize list of authors from get all Books step and compose authors shelves; maybe series name? (foundation)
    //TODO: space: 5 -> wtf?? same with classics || dystopian?? nu apare

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

    public static List<String> getTopShelves(final List<Shelve> allShelves) {
        final List<Shelve> normalizedAndFilteredShelves = allShelves.stream()
                .filter(ShelveAggregator::shelveNotInFilterList)
                .filter(ShelveAggregator::shelveNotInStartsWithFilterList)
                .map(ShelveAggregator::normalizeShelveName)
                .collect(toList());

        final Map<String, Integer> shelvesPopularityMap = normalizedAndFilteredShelves.stream()
                .collect(Collectors.groupingBy(Shelve::getName, Collectors.summingInt(Shelve::getPopularity)));

        return shelvesPopularityMap.entrySet().stream()
                .sorted(comparing(Map.Entry::getValue, reverseOrder()))
                .limit(SHELVE_LIMIT)
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    private static Shelve normalizeShelveName(final Shelve shelve) {
        SHELVE_NAME_MAPPINGS.stream()
                .filter(mapping -> mapping.getPotentialValues().contains(shelve.getName()))
                .findFirst()
                .ifPresent(mapping -> shelve.setName(mapping.getNormalizedValue()));

        return shelve;
    }

    private static boolean shelveNotInFilterList(final Shelve shelve) {
        return !SHELVE_NAMES_FILTER.contains(shelve.getName());
    }

    private static boolean shelveNotInStartsWithFilterList(final Shelve shelve) {
        return SHELVE_NAMES_STARTS_WITH_FILTER.stream()
                .noneMatch(shelveNameToFilter -> shelve.getName().startsWith(shelveNameToFilter));
    }

    static class ShelveMapping {

        private String normalizedValue;

        private List<String> potentialValues;

        ShelveMapping(final String normalizedValue, final List<String> potentialValues) {
            this.normalizedValue = normalizedValue;
            this.potentialValues = potentialValues;
        }

        String getNormalizedValue() {
            return normalizedValue;
        }

        List<String> getPotentialValues() {
            return potentialValues;
        }
    }

}
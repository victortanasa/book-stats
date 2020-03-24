package service;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

import model.Shelve;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ShelveAnalyzer {

    //TODO: maybe top category, sub-category

    //TODO: Docs
    //TODO: add more shelve mappings
    private static final List<String> SHELVES_TO_FILTER = newArrayList("to-read", "currently-reading", "owned", "default", "favorites",
            "books-i-own", "ebook", "kindle", "library", "audiobook", "owned-books", "audiobooks", "my-books", "ebooks", "to-buy",
            "english", "calibre", "books", "british", "audio", "my-library", "favourites", "re-read", "general", "e-books",
            "audible", "book-club", "series", "sf-masterworks", "abandoned", "dnf", "the-expanse", "asimov");

    private static final int SHELVE_LIMIT = 3;

    private static final List<ShelveMapping> SHELVE_NAME_MAPPINGS;

    static {
        SHELVE_NAME_MAPPINGS = newArrayList();
        SHELVE_NAME_MAPPINGS.add(new ShelveMapping("sci-fi", newArrayList("science-fiction", "scifi", "sf", "sci-fi")));
        SHELVE_NAME_MAPPINGS.add(new ShelveMapping("mil-sci-fi", newArrayList("military-sci-fi", "military-science-fiction", "mil-sci-fi")));
        SHELVE_NAME_MAPPINGS.add(new ShelveMapping("sci-fi-fantasy", newArrayList("sci-fi-fantasy", "scifi-fantasy")));
        SHELVE_NAME_MAPPINGS.add(new ShelveMapping("dystopian", newArrayList("dystopian", "dystopia"))); // post-apocalyptic?
    }

    static List<String> getTopShelves(final List<Shelve> allShelves) {
        final List<Shelve> shelvesWithNormalizedNames = allShelves.stream()
                .filter(shelve -> !SHELVES_TO_FILTER.contains(shelve.getName()))
                .map(ShelveAnalyzer::normalizeShelveName)
                .collect(toList());

        final Map<String, Integer> shelvesPopularityMap = shelvesWithNormalizedNames.stream()
                .collect(Collectors.groupingBy(Shelve::getName, Collectors.summingInt(Shelve::getPopularity)));

        return shelvesPopularityMap.entrySet().stream()
                .sorted(comparing(Map.Entry::getValue, reverseOrder()))
                .limit(SHELVE_LIMIT)
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    private static Shelve normalizeShelveName(final Shelve shelve) {
        for (final ShelveMapping mapping : SHELVE_NAME_MAPPINGS) {
            if (mapping.getPotentialValues().contains(shelve.getName())) {
                return new Shelve(mapping.getNormalizedValue(), shelve.getPopularity());
            }
        }
        return shelve;
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
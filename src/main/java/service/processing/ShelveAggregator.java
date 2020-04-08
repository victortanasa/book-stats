package service.processing;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

import model.Shelve;
import model.ShelveMapping;
import service.StorageService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShelveAggregator {

    private static final int SHELVE_LIMIT = 3;

    //TODO: something about constructors - consistency
    private static final StorageService STORAGE_SERVICE = new StorageService();

    private List<ShelveMapping> shelveMappings;

    private List<String> shelveToExclude;
    private List<String> shelveToExcludeThatStartWith;

    //TODO: Docs
    public ShelveAggregator() {
        shelveMappings = STORAGE_SERVICE.loadShelveMappings();
        shelveToExclude = STORAGE_SERVICE.getShelvesToExclude();
        shelveToExcludeThatStartWith = STORAGE_SERVICE.getShelvesToExcludeThatStartWith();
    }

    public List<String> getTopShelves(final List<Shelve> allShelves) {
        final List<Shelve> normalizedAndFilteredShelves = allShelves.stream()
                .filter(this::shelveNotExcluded)
                .filter(this::shelveDoesNotStartWithExcludedWord)
                .map(this::normalizeShelveName)
                .collect(toList());

        final Map<String, Integer> shelvesPopularityMap = normalizedAndFilteredShelves.stream()
                .collect(Collectors.groupingBy(Shelve::getName, Collectors.summingInt(Shelve::getPopularity)));

        return shelvesPopularityMap.entrySet().stream()
                .sorted(comparing(Map.Entry::getValue, reverseOrder()))
                .limit(SHELVE_LIMIT)
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    private Shelve normalizeShelveName(final Shelve shelve) {
        shelveMappings.stream()
                .filter(mapping -> mapping.getAlternateValues().contains(shelve.getName()))
                .findFirst()
                .ifPresent(mapping -> shelve.setName(mapping.getNormalizedValue()));

        return shelve;
    }

    private boolean shelveNotExcluded(final Shelve shelve) {
        return !shelveToExclude.contains(shelve.getName());
    }

    private boolean shelveDoesNotStartWithExcludedWord(final Shelve shelve) {
        return shelveToExcludeThatStartWith.stream()
                .noneMatch(shelveNameToFilter -> shelve.getName().startsWith(shelveNameToFilter));
    }

}
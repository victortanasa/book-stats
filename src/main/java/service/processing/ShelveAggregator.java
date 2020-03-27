package service.processing;

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

    private static final int SHELVE_LIMIT = 3;

    private static final StorageService STORAGE_SERVICE = new StorageService();

    private List<ShelveMapping> shelveMappings;

    private List<String> shelveToExclude;
    private List<String> shelveToExcludeThatStartWith;

    //TODO: Docs
    //TODO: maybe top category, sub-category
    //TODO: initialize list of authors from get all Books step and compose authors shelves; maybe series name? (foundation)
    public ShelveAggregator() {
        shelveMappings = STORAGE_SERVICE.loadShelveMappings();
        shelveToExclude = STORAGE_SERVICE.getShelvesToExclude();
        shelveToExcludeThatStartWith = STORAGE_SERVICE.getShelvesToExcludeThatStartWith();
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
        return !shelveToExclude.contains(userShelve.getName());
    }

    private boolean shelveNotInStartsWithFilterList(final UserShelve userShelve) {
        return shelveToExcludeThatStartWith.stream()
                .noneMatch(shelveNameToFilter -> userShelve.getName().startsWith(shelveNameToFilter));
    }

}
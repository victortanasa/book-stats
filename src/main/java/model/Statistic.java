package model;

import model.enums.StatisticType;
import model.enums.sort.SortBy;
import model.enums.sort.SortOrder;

public class Statistic {

    private String name;

    private StatisticType type;

    private SortBy sortBy;

    private SortOrder sortOrder;

    public Statistic(final String name, final StatisticType type) {
        this.name = name;
        this.type = type;
    }

    public Statistic(final String name, final StatisticType type, final SortBy sortBy, final SortOrder sortOrder) {
        this.sortOrder = sortOrder;
        this.sortBy = sortBy;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public StatisticType getType() {
        return type;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

}
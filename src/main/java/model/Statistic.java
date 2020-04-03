package model;

import model.enums.StatisticType;
import model.enums.sort.SortBy;
import model.enums.sort.SortOrder;
import utils.TransformationUtils;

public class Statistic {

    private String name;
    //TODO: add to constructor
    private String keyLabel;
    private String valueLabel;

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

    public String getFileName() {
        return TransformationUtils.getCamelCase(name);
    }

    public String getKeyLabel() {
        return keyLabel;
    }

    public String getValueLabel() {
        return valueLabel;
    }

    public Statistic withStatisticKeyName(final String keyLabel) {
        this.keyLabel = keyLabel;
        return this;
    }

    public Statistic withStatisticValueName(final String valueLabel) {
        this.valueLabel = valueLabel;
        return this;
    }

}
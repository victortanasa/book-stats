package model;

import model.enums.StatisticType;
import model.enums.sort.SortBy;
import model.enums.sort.SortOrder;
import utils.TransformationUtils;

public class Statistic {

    private static final int DEFAULT_RESULT_LIMIT = 1000;

    private String name;
    //TODO: add to constructor
    private String keyLabel;
    private String valueLabel;

    private StatisticType type;

    private SortBy sortBy;

    private SortOrder sortOrder;

    private Integer resultLimit;

    public Statistic(final String name, final StatisticType type) {
        this.name = name;
        this.type = type;
    }

    public Statistic(final String name, final StatisticType type, final SortBy sortBy, final SortOrder sortOrder) {
        this.resultLimit = DEFAULT_RESULT_LIMIT;
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

    public Integer getResultLimit() {
        return resultLimit;
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

    public Statistic withResultLimit(final Integer resultLimit) {
        this.resultLimit = resultLimit;
        return this;
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
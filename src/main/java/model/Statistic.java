package model;

import model.enums.StatisticType;
import model.enums.sort.SortBy;
import model.enums.sort.SortOrder;
import utils.TransformationUtils;

public class Statistic {

    private static final String FILE_NAME_FORMAT = "%s%s";
    private static final int DEFAULT_RESULT_LIMIT = 1000;

    private String name;

    private StatisticType type;

    private SortBy sortBy;

    private SortOrder sortOrder;

    private Integer resultLimit;

    //TODO: maybe a better way?
    private String statisticKeyName;
    private String statisticValueName;

    //TODO: filter -> authors have more than x books for Average page number for authors

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
        return String.format(FILE_NAME_FORMAT,
                TransformationUtils.getCamelCase(statisticKeyName), TransformationUtils.getCamelCase(statisticValueName));
    }

    public String getStatisticKeyName() {
        return statisticKeyName;
    }

    public String getStatisticValueName() {
        return statisticValueName;
    }

    public Statistic withResultLimit(final Integer resultLimit) {
        this.resultLimit = resultLimit;
        return this;
    }

    public Statistic withStatisticKeyName(final String statisticKeyName) {
        this.statisticKeyName = statisticKeyName;
        return this;
    }

    public Statistic withStatisticValueName(final String statisticValueName) {
        this.statisticValueName = statisticValueName;
        return this;
    }

}
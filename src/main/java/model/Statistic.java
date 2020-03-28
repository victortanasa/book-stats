package model;

import model.enums.StatisticType;

public class Statistic {

    private String name;

    private StatisticType type;

    public Statistic(final String name, final StatisticType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public StatisticType getType() {
        return type;
    }
}
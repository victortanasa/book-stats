package model;

import model.enums.StatisticName;
import model.enums.StatisticType;

public class Statistic {

    private StatisticName statisticName;

    private StatisticType statisticType;

    public Statistic(final StatisticName statisticName, final StatisticType statisticType) {
        this.statisticName = statisticName;
        this.statisticType = statisticType;
    }

    public StatisticName getStatisticName() {
        return statisticName;
    }

    public StatisticType getStatisticType() {
        return statisticType;
    }
}
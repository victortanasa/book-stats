package service;

import static com.google.common.collect.Lists.newArrayList;
import static model.enums.StatisticName.MOST_READ_AUTHORS_BY_BOOK_COUNT;
import static model.enums.StatisticType.MAP;

import model.Statistic;

import java.util.List;

public class StatisticsHolder {

    private static final List<Statistic> STATISTICS;

    static {
        STATISTICS = newArrayList();
        STATISTICS.add(new Statistic(MOST_READ_AUTHORS_BY_BOOK_COUNT, MAP));
    }

}
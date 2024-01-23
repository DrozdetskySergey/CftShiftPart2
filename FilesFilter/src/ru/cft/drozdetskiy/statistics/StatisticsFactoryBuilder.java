package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.statistics.impl.FullStatisticsFactory;
import ru.cft.drozdetskiy.statistics.impl.SimpleStatisticsFactory;

public class StatisticsFactoryBuilder {

    public static StatisticsFactory buildFactory(StatisticsType type) {
        return type == StatisticsType.FULL ? new FullStatisticsFactory() : new SimpleStatisticsFactory();
    }
}

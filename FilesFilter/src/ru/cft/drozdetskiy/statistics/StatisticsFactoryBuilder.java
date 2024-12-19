package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.statistics.impl.FullStatisticsFactory;
import ru.cft.drozdetskiy.statistics.impl.SimpleStatisticsFactory;

public final class StatisticsFactoryBuilder {

    public static StatisticsFactory build(StatisticsType type) {
        return type == StatisticsType.FULL ? new FullStatisticsFactory() : new SimpleStatisticsFactory();
    }
}

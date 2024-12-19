package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

final public class SimpleStatisticsFactory implements StatisticsFactory {

    @Override
    public Statistics<Long> createForLong() {
        return new SimpleLongsStatistics();
    }

    @Override
    public Statistics<Double> createForDouble() {
        return new SimpleDoublesStatistics();
    }

    @Override
    public Statistics<String> createForString() {
        return new SimpleStringsStatistics();
    }
}

package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

public class FullStatisticsFactory implements StatisticsFactory {

    @Override
    public Statistics<Long> createForInteger() {
        return new FullLongsStatistics();
    }

    @Override
    public Statistics<Double> createForDouble() {
        return new FullDoublesStatistics();
    }

    @Override
    public Statistics<String> createForString() {
        return new FullStringsStatistics();
    }
}

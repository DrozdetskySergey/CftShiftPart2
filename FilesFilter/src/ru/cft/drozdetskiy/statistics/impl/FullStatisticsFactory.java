package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

/**
 * Фабрика для создания объектов интерфейса {@link Statistics} параметризованных типами: {@link Long}, {@link Double}, {@link String}.
 * Создаёт новые объекты для сбора полной (FULL) статистики.
 */
public final class FullStatisticsFactory implements StatisticsFactory {

    @Override
    public Statistics<Long> createForLong() {
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

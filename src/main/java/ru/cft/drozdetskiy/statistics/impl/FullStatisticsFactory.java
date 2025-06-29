package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

/**
 * Фабрики реализующий интерфейс абстрактной фабрики {@link StatisticsFactory}.
 * Создаёт новые объекты для сбора полной (FULL) {@linkplain  Statistics статистики}
 * параметризованных типами: {@link Long}, {@link Double}, {@link String}.
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

package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

/**
 * Фабрики реализующий интерфейс абстрактной фабрики {@link StatisticsFactory}.
 * Создаёт новые объекты для сбора краткой (SIMPLE) {@linkplain  Statistics статистики}
 * параметризованных типами: {@link Long}, {@link Double}, {@link String}.
 */
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

package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

/**
 * Фабрика для создания объектов интерфейса {@link Statistics} параметризованных типами: {@link Long}, {@link Double}, {@link String}.
 * Создаёт новые объекты для сбора краткой (SIMPLE) статистики.
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

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
    public Statistics<Long> createForInteger() {
        return new FullIntegersStatistics();
    }

    @Override
    public Statistics<Double> createForFloat() {
        return new FullFloatsStatistics();
    }

    @Override
    public Statistics<String> createForString() {
        return new FullStringsStatistics();
    }
}

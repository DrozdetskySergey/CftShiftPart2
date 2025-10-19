package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsType;

/**
 * Фабрики реализующий интерфейс абстрактной фабрики {@link StatisticsFactory}.
 * Создаёт новые объекты для сбора {@linkplain StatisticsType#SIMPLE краткой} статистики.
 */
final public class SimpleStatisticsFactory implements StatisticsFactory {

    @Override
    public Statistics createForInteger() {
        return new SimpleIntegersStatistics();
    }

    @Override
    public Statistics createForFloat() {
        return new SimpleFloatsStatistics();
    }

    @Override
    public Statistics createForString() {
        return new SimpleStringsStatistics();
    }
}

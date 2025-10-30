package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsType;

/**
 * Фабрика реализующая интерфейс абстрактной фабрики {@link StatisticsFactory}.
 * Создаёт новые объекты для сбора {@linkplain StatisticsType#FULL полной} статистики.
 */
final public class FullStatisticsFactory implements StatisticsFactory {

    @Override
    public Statistics createForInteger() {
        return new FullIntegersStatistics();
    }

    @Override
    public Statistics createForFloat() {
        return new FullFloatsStatistics();
    }

    @Override
    public Statistics createForString() {
        return new FullStringsStatistics();
    }
}

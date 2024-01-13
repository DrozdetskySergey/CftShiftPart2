package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.statistics.impl.FullStatisticsFactory;
import ru.cft.drozdetskiy.statistics.impl.SimpleStatisticsFactory;

public class StatisticsFactoryBuilder {

    public static StatisticsFactory buildFactory(StatisticsType type) throws StatisticsException {
        StatisticsFactory result = null;

        if (type == StatisticsType.SIMPLE) {
            result = new SimpleStatisticsFactory();
        } else if (type == StatisticsType.FULL) {
            result = new FullStatisticsFactory();
        } else {
            throw new StatisticsException("Не известный тип статистики.");
        }

        return result;
    }
}

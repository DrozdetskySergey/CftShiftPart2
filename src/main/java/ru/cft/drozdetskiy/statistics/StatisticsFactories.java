package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.statistics.impl.FullStatisticsFactory;
import ru.cft.drozdetskiy.statistics.impl.SimpleStatisticsFactory;

/**
 * Функциональный класс. Предоставляет статичный метод {@linkplain #get(StatisticsType) get}
 */
public final class StatisticsFactories {

    private StatisticsFactories() {
    }

    /**
     * Создаёт объект интерфейса {@link StatisticsFactory} относительно переданного типа статистики.
     *
     * @param type требуемый тип статистики {@linkplain StatisticsType}
     * @return объект конкретной фабрики реализующий интерфейс {@link StatisticsFactory}
     */
    public static StatisticsFactory get(StatisticsType type) {
        return type == StatisticsType.FULL ? new FullStatisticsFactory() : new SimpleStatisticsFactory();
    }
}

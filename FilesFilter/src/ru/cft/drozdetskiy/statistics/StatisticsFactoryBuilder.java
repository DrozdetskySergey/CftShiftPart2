package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.statistics.impl.FullStatisticsFactory;
import ru.cft.drozdetskiy.statistics.impl.SimpleStatisticsFactory;

/**
 * Функциональный класс. Предоставляет статичный метод {@linkplain #of(StatisticsType) of}
 */
public final class StatisticsFactoryBuilder {

    private StatisticsFactoryBuilder() {
    }

    /**
     * Создаёт объект интерфейса {@link StatisticsFactory} относительно переданного типа статистики.
     *
     * @param type требуемый тип статистики.
     * @return объект конкретной фабрики реализующий интерфейс {@link StatisticsFactory}.
     */
    public static StatisticsFactory of(StatisticsType type) {
        return type == StatisticsType.FULL ? new FullStatisticsFactory() : new SimpleStatisticsFactory();
    }
}

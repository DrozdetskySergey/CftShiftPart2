package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.statistics.impl.FullStatisticsFactory;
import ru.cft.drozdetskiy.statistics.impl.SimpleStatisticsFactory;

/**
 * Функциональный класс. Специализируется на создание объектов абстрактной фабрики {@link StatisticsFactory}.
 * Предоставляет статичный метод {@linkplain #get(StatisticsType) get}
 */
public final class StatisticsFactories {

    private StatisticsFactories() {
    }

    /**
     * Создаёт объект конкретной фабрики реализующий интерфейс абстрактной фабрики {@link StatisticsFactory}
     * в зависимости от требуемого {@linkplain StatisticsType типа статистики}.
     *
     * @param type требуемый {@linkplain StatisticsType тип статистики}
     * @return Объект конкретной фабрики реализующий интерфейс абстрактной фабрики {@link StatisticsFactory}.
     */
    public static StatisticsFactory get(StatisticsType type) {
        return switch (type) {
            case SIMPLE -> new SimpleStatisticsFactory();
            case FULL -> new FullStatisticsFactory();
        };
    }
}

package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;
import ru.cft.drozdetskiy.statistics.impl.*;

/**
 * Фабрика для создания объектов интерфейса {@link Statistics} специализирующихся на всех {@linkplain  ContentType типах}
 * содержимого строки.
 * Реализует метод {@linkplain #createFor(ContentType) createFor(ContentType)}
 * Существует две фабрики: {@linkplain #SIMPLE} и {@linkplain #FULL}.
 */
public enum StatisticsFactory {

    /**
     * Создаёт новые объекты для сбора краткой статистики.
     */
    SIMPLE {
        @Override
        public Statistics createFor(ContentType type) {
            return switch (type) {
                case INTEGER -> new SimpleIntegersStatistics();
                case FLOAT -> new SimpleFloatsStatistics();
                case STRING -> new SimpleStringsStatistics();
            };
        }
    },
    /**
     * Создаёт новые объекты для сбора полной статистики.
     */
    FULL {
        @Override
        public Statistics createFor(ContentType type) {
            return switch (type) {
                case INTEGER -> new FullIntegersStatistics();
                case FLOAT -> new FullFloatsStatistics();
                case STRING -> new FullStringsStatistics();
            };
        }
    };

    /**
     * Создаёт новый объект интерфейса {@link Statistics} для типа {@linkplain ContentType#INTEGER INTEGER}
     *
     * @return {@link Statistics}
     */
    public abstract Statistics createFor(ContentType type);
}

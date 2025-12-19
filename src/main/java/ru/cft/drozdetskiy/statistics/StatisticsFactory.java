package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Фабрика для создания объектов интерфейса {@link Statistics} для любого {@linkplain ContentType типа} содержимого строки.
 * Реализует метод {@linkplain #createFor(ContentType)}.
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
     * Создаёт новый объект интерфейса {@link Statistics} для любого {@linkplain ContentType типа} содержимого строки.
     *
     * @param type {@linkplain ContentType тип} содержимого строки.
     * @return Объект интерфейса {@link Statistics}.
     */
    public abstract Statistics createFor(ContentType type);
}

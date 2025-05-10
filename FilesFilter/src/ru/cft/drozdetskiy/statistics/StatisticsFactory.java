package ru.cft.drozdetskiy.statistics;

/**
 * Абстрактная фабрика для создания объектов интерфейса {@link Statistics} параметризованных типами: {@link Long}, {@link Double}, {@link String}.
 * Предоставляет методы:
 * {@linkplain #createForLong() createForLong},
 * {@linkplain #createForDouble() createForDouble},
 * {@linkplain #createForString() createForString}.
 */
public interface StatisticsFactory {

    /**
     * Создаёт новый объект статистики {@link Statistics} параметризованный типом {@link Long}
     *
     * @return {@link Statistics}<{@link Long}>
     */
    Statistics<Long> createForLong();

    /**
     * Создаёт новый объект статистики {@link Statistics} параметризованный типом {@link Double}
     *
     * @return {@link Statistics}<{@link Double}>
     */
    Statistics<Double> createForDouble();

    /**
     * Создаёт новый объект статистики {@link Statistics} параметризованный типом {@link String}
     *
     * @return {@link Statistics}<{@link String}>
     */
    Statistics<String> createForString();
}

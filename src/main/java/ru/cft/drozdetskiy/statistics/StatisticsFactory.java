package ru.cft.drozdetskiy.statistics;

/**
 * Абстрактная фабрика для создания объектов интерфейса {@link Statistics}
 * параметризованных типами: {@link Long}, {@link Double}, {@link String}.
 * Предоставляет методы:
 * {@linkplain #createForInteger() createForLong},
 * {@linkplain #createForFloat() createForDouble},
 * {@linkplain #createForString() createForString}.
 */
public interface StatisticsFactory {

    /**
     * Создаёт новый объект интерфейса {@link Statistics} параметризованный типом {@link Long}
     *
     * @return {@link Statistics}<{@link Long}>
     */
    Statistics<Long> createForInteger();

    /**
     * Создаёт новый объект интерфейса {@link Statistics} параметризованный типом {@link Double}
     *
     * @return {@link Statistics}<{@link Double}>
     */
    Statistics<Double> createForFloat();

    /**
     * Создаёт новый объект интерфейса {@link Statistics} параметризованный типом {@link String}
     *
     * @return {@link Statistics}<{@link String}>
     */
    Statistics<String> createForString();
}

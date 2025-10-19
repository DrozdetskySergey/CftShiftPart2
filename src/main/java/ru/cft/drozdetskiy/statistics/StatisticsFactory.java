package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Абстрактная фабрика для создания объектов интерфейса {@link Statistics}
 * для разных значений определенного {@linkplain  ContentType типа}.
 * Предоставляет методы:
 * {@linkplain #createForInteger() createForInteger},
 * {@linkplain #createForFloat() createForFloat},
 * {@linkplain #createForString() createForString}.
 */
public interface StatisticsFactory {

    /**
     * Создаёт новый объект интерфейса {@link Statistics} для конкретного {@linkplain ContentType#INTEGER типа INTEGER}
     *
     * @return {@link Statistics}
     */
    Statistics createForInteger();

    /**
     * Создаёт новый объект интерфейса {@link Statistics} для конкретного {@linkplain ContentType#FLOAT типа FLOAT}
     *
     * @return {@link Statistics}
     */
    Statistics createForFloat();

    /**
     * Создаёт новый объект интерфейса {@link Statistics} для конкретного {@linkplain ContentType#STRING типа STRING}
     *
     * @return {@link Statistics}
     */
    Statistics createForString();
}

package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Абстрактная фабрика для создания объектов интерфейса {@link Statistics} для всех значений {@linkplain  ContentType типа}.
 * Предоставляет методы:
 * {@linkplain #createForInteger() createForInteger},
 * {@linkplain #createForFloat() createForFloat},
 * {@linkplain #createForString() createForString}.
 */
public interface StatisticsFactory {

    /**
     * Создаёт новый объект интерфейса {@link Statistics} для типа {@linkplain ContentType#INTEGER INTEGER}
     *
     * @return {@link Statistics}
     */
    Statistics createForInteger();

    /**
     * Создаёт новый объект интерфейса {@link Statistics} для типа {@linkplain ContentType#FLOAT FLOAT}
     *
     * @return {@link Statistics}
     */
    Statistics createForFloat();

    /**
     * Создаёт новый объект интерфейса {@link Statistics} для типа {@linkplain ContentType#STRING STRING}
     *
     * @return {@link Statistics}
     */
    Statistics createForString();
}

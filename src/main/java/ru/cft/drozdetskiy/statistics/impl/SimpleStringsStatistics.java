package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.ContentType;
import ru.cft.drozdetskiy.statistics.Statistics;

/**
 * Краткая статистика для значения типа {@linkplain ContentType#STRING STRING} переданного в формате строки.
 */
final class SimpleStringsStatistics implements Statistics {

    /**
     * Количество.
     */
    private long count;

    @Override
    public void include(String value) {
        count++;
    }

    @Override
    public String toString() {
        return String.format("Количество строк = %d%n", count);
    }
}

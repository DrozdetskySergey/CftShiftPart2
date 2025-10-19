package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.ContentType;
import ru.cft.drozdetskiy.statistics.Statistics;

/**
 * Краткая статистика для конкретного значения {@linkplain ContentType#INTEGER типа INTEGER} переданного в виде строки.
 */
final class SimpleIntegersStatistics implements Statistics {

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
        return String.format("Количество целых чисел = %d%n", count);
    }
}

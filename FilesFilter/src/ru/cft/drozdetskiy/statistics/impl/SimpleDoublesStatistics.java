package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

/**
 * Краткая статистика по объектам {@link Double}
 */
final class SimpleDoublesStatistics implements Statistics<Double> {

    /**
     * Количество.
     */
    private long count = 0;

    @Override
    public void include(Double value) {
        count++;
    }

    @Override
    public String toString() {
        return String.format("Количество вещественных чисел = %d%n", count);
    }
}

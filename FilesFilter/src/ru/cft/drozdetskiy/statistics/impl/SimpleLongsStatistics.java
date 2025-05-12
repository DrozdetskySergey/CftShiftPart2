package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

/**
 * Краткая статистика по объектам класса {@link Long}
 */
final class SimpleLongsStatistics implements Statistics<Long> {

    /**
     * Количество.
     */
    private long count = 0;

    @Override
    public void include(Long value) {
        count++;
    }

    @Override
    public String toString() {
        return String.format("Количество целых чисел = %d%n", count);
    }
}

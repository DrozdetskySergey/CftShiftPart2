package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

final class SimpleDoublesStatistics implements Statistics<Double> {

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

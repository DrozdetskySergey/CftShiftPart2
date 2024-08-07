package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

class SimpleDoublesStatistics implements Statistics<Double> {

    private int count;

    @Override
    public void include(Double value) {
        count++;
    }

    @Override
    public String toString() {
        return String.format("Количество вещественных чисел = %d%n", count);
    }
}

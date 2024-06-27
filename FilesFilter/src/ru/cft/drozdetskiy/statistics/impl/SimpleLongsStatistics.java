package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

class SimpleLongsStatistics implements Statistics<Long> {

    private int count;

    @Override
    public void include(Long value) {
        count++;
    }

    @Override
    public String getInfo() {
        return String.format("Количество целых чисел = %d%n", count);
    }
}

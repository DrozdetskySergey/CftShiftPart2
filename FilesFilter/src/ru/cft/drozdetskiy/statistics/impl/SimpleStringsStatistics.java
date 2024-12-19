package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

final class SimpleStringsStatistics implements Statistics<String> {

    private long count = 0;

    @Override
    public void include(String value) {
        count++;
    }

    @Override
    public String toString() {
        return String.format("Количество строк = %d%n", count);
    }
}

package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

class SimpleStringsStatistics implements Statistics<String> {

    private int count;

    @Override
    public void include(String value) {
        count++;
    }

    @Override
    public String getInfo() {
        return String.format("Количество строк = %d%n", count);
    }
}

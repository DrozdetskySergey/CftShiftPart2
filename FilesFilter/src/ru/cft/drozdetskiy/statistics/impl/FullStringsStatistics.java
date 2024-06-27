package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

public class FullStringsStatistics implements Statistics<String> {

    private int minLength = Integer.MAX_VALUE;
    private int maxLength = Integer.MIN_VALUE;
    private int count;

    @Override
    public void include(String value) {
        if (value.length() < minLength) {
            minLength = value.length();
        }

        if (value.length() > maxLength) {
            maxLength = value.length();
        }

        count++;
    }

    @Override
    public String getInfo() {
        StringBuilder result = new StringBuilder(String.format("Количество сток = %d%n", count));

        if (count > 0) {
            result.append(String.format("| Минимальная длина строки = %d%n", minLength));
            result.append(String.format("| Максимальная длина строки = %d%n", maxLength));
        }

        return result.toString();
    }
}

package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

public class FullStringsStatistics implements Statistics<String> {

    private int minLength;
    private int maxLength;
    private int count;

    public FullStringsStatistics() {
        minLength = Integer.MAX_VALUE;
        maxLength = Integer.MIN_VALUE;
    }

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
        String result = String.format("Количество элементов типа String = %d%n", count);

        if (count > 0) {
            result += String.format("| Минимальная длина строки = %d%n", minLength) +
                    String.format("| Максимальная длина строки = %d%n", maxLength);
        }

        return result;
    }
}

package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

/**
 * Полная статистика по объектам класса {@link String}
 */
final public class FullStringsStatistics implements Statistics<String> {

    /**
     * Минимальная длина строки.
     */
    private int minLength = Integer.MAX_VALUE;
    /**
     * Максимальная длина строки.
     */
    private int maxLength = 0;
    /**
     * Количество.
     */
    private long count = 0;

    @Override
    public void include(String value) {
        int length = value.length();

        if (length < minLength) {
            minLength = length;
        }

        if (length > maxLength) {
            maxLength = length;
        }

        count++;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("Количество сток = %d%n", count));

        if (count > 0) {
            result.append(String.format("| Минимальная длина строки = %d%n", minLength));
            result.append(String.format("| Максимальная длина строки = %d%n", maxLength));
        }

        return result.toString();
    }
}

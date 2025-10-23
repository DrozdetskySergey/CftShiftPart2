package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.ContentType;
import ru.cft.drozdetskiy.statistics.Statistics;

/**
 * Полная статистика для конкретного значения {@linkplain ContentType#STRING типа STRING} переданного в виде строки.
 */
final public class FullStringsStatistics implements Statistics {

    /**
     * Минимальная длина строки.
     */
    private int minLength = Integer.MAX_VALUE;
    /**
     * Максимальная длина строки.
     */
    private int maxLength;
    /**
     * Количество.
     */
    private long count;

    @Override
    public void include(String value) {
        minLength = Math.min(minLength, value.length());
        maxLength = Math.max(maxLength, value.length());
        count++;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("Количество строк = %d%n", count));

        if (count > 0) {
            result.append(String.format("| Минимальная длина строки = %d%n", minLength));
            result.append(String.format("| Максимальная длина строки = %d%n", maxLength));
        }

        return result.toString();
    }
}

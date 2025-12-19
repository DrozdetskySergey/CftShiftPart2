package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Полная статистика для значения типа {@linkplain ContentType#STRING STRING} переданного в формате строки.
 */
final class FullStringsStatistics implements Statistics {

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

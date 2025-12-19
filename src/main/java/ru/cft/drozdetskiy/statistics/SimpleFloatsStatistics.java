package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Краткая статистика для значения типа {@linkplain ContentType#FLOAT FLOAT} переданного в формате строки.
 */
final class SimpleFloatsStatistics implements Statistics {

    /**
     * Количество.
     */
    private long count;

    @Override
    public void include(String value) {
        count++;
    }

    @Override
    public String toString() {
        return String.format("Количество вещественных чисел = %d%n", count);
    }
}

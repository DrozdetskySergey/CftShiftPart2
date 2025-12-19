package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Краткая статистика для значения типа {@linkplain ContentType#INTEGER INTEGER} переданного в формате строки.
 */
final class SimpleIntegersStatistics implements Statistics {

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
        return String.format("Количество целых чисел = %d%n", count);
    }
}

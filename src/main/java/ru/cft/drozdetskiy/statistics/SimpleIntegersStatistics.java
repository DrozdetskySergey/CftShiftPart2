package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Краткая статистика для значения типа {@linkplain ContentType#INTEGER INTEGER} переданного в формате строки.
 */
final class SimpleIntegersStatistics extends Statistics {

    /**
     * Краткая статистика для значения типа {@linkplain ContentType#INTEGER INTEGER} переданного в формате строки.
     */
    public SimpleIntegersStatistics() {
        super(ContentType.INTEGER);
    }

    @Override
    public void include(String value) {
        count++;
    }

    @Override
    public String getAdditionalInfo() {
        return "";
    }

    @Override
    public String toString() {
        return String.format("Количество целых чисел = %d%n", count);
    }
}

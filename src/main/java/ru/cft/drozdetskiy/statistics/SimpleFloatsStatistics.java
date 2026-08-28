package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Краткая статистика для значения типа {@linkplain ContentType#FLOAT FLOAT} переданного в формате строки.
 */
final class SimpleFloatsStatistics extends Statistics {

    /**
     * Краткая статистика для значения типа {@linkplain ContentType#FLOAT FLOAT} переданного в формате строки.
     */
    public SimpleFloatsStatistics() {
        super(ContentType.FLOAT);
    }

    @Override
    public void include(String value) {
        count++;
    }

    @Override
    public String getAdditionalInfo() {
        return "";
    }
}

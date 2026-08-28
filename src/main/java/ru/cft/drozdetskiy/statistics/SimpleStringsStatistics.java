package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Краткая статистика для значения типа {@linkplain ContentType#STRING STRING} переданного в формате строки.
 */
final class SimpleStringsStatistics extends Statistics {

    /**
     * Краткая статистика для значения типа {@linkplain ContentType#STRING STRING} переданного в формате строки.
     */
    public SimpleStringsStatistics() {
        super(ContentType.STRING);
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

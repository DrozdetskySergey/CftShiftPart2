package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Полная статистика для значения типа {@linkplain ContentType#STRING STRING} переданного в формате строки.
 */
final class FullStringsStatistics extends Statistics {

    /**
     * Минимальная длина строки.
     */
    private int minLength;
    /**
     * Максимальная длина строки.
     */
    private int maxLength;

    /**
     * Полная статистика для значения типа {@linkplain ContentType#STRING STRING} переданного в формате строки.
     */
    public FullStringsStatistics() {
        super(ContentType.STRING);

        minLength = Integer.MAX_VALUE;
        maxLength = 0;
    }

    @Override
    public void include(String value) {
        minLength = Math.min(minLength, value.length());
        maxLength = Math.max(maxLength, value.length());
        count++;
    }

    @Override
    public String getAdditionalInfo() {
        StringBuilder result = new StringBuilder();

        if (count > 0) {
            result.append(String.format("Минимальная длина строки = %d%n", minLength));
            result.append(String.format("Максимальная длина строки = %d%n", maxLength));
        }

        return result.toString();
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

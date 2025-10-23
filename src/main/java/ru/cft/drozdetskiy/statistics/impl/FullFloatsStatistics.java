package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.ContentType;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Полная статистика для конкретного значения {@linkplain ContentType#FLOAT типа FLOAT} переданного в виде строки.
 */
final class FullFloatsStatistics implements Statistics {

    /**
     * Минимальное значение.
     */
    private BigDecimal minDecimal;
    /**
     * Максимальное значение.
     */
    private BigDecimal maxDecimal;
    /**
     * Сумма.
     */
    private BigDecimal sum = BigDecimal.ZERO;
    /**
     * Флаг. Сигнализирует, что сумма не имеет большого масштаба по модулю.
     */
    private boolean hasNormalScale = true;
    /**
     * Количество.
     */
    private long count;

    @Override
    public void include(String value) {
        BigDecimal decimal = new BigDecimal(value);
        minDecimal = minDecimal == null ? decimal : minDecimal.min(decimal);
        maxDecimal = maxDecimal == null ? decimal : maxDecimal.max(decimal);
        final int greatestScale = 100000;

        if (hasNormalScale && Math.abs(decimal.scale()) < greatestScale) {
            sum = sum.add(decimal);
        } else {
            hasNormalScale = false;
        }

        count++;
    }

    @Override
    public String toString() {
        final String message = "Сбой. Переполнение поддерживаемого диапазона.";
        StringBuilder result = new StringBuilder(String.format("Количество вещественных чисел = %d%n", count));

        if (count > 0) {
            sum = sum.setScale(16, RoundingMode.DOWN);
            BigDecimal average = sum.divide(BigDecimal.valueOf(count), RoundingMode.DOWN);
            result.append(String.format("| Минимальное значение = %e%n", minDecimal));
            result.append(String.format("| Максимальное значение = %e%n", maxDecimal));
            result.append(String.format("| Среднее арифметическое значение = %s%n", hasNormalScale ? average : message));
            result.append(String.format("| Сумма всех вещественных чисел = %s%n", hasNormalScale ? sum : message));
        }

        return result.toString();
    }
}

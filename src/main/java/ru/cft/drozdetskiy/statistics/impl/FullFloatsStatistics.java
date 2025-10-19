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
    private BigDecimal min;
    /**
     * Максимальное значение.
     */
    private BigDecimal max;
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
        BigDecimal decimalNumber = new BigDecimal(value);
        min = min == null ? decimalNumber : min.min(decimalNumber);
        max = max == null ? decimalNumber : max.max(decimalNumber);
        final int maxNormalScale = 100000;

        if (hasNormalScale && Math.abs(decimalNumber.scale()) < maxNormalScale) {
            sum = sum.add(decimalNumber);
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
            result.append(String.format("| Минимальное значение = %e%n", min));
            result.append(String.format("| Максимальное значение = %e%n", max));
            result.append(String.format("| Среднее арифметическое значение = %s%n", hasNormalScale ? average : message));
            result.append(String.format("| Сумма всех элементов = %s%n", hasNormalScale ? sum : message));
        }

        return result.toString();
    }
}

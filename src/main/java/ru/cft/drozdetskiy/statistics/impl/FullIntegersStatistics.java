package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.ContentType;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Полная статистика для конкретного значения {@linkplain ContentType#INTEGER типа INTEGER} переданного в виде строки.
 */
final class FullIntegersStatistics implements Statistics {

    /**
     * Минимальное значение.
     */
    private BigInteger min;
    /**
     * Максимальное значение.
     */
    private BigInteger max;
    /**
     * Сумма.
     */
    private BigInteger sum = BigInteger.ZERO;
    /**
     * Количество.
     */
    private long count;

    @Override
    public void include(String value) {
        BigInteger number = new BigInteger(value);
        min = min == null ? number : min.min(number);
        max = max == null ? number : max.max(number);
        sum = sum.add(number);
        count++;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("Количество целых чисел = %d%n", count));

        if (count > 0) {
            BigDecimal average = (new BigDecimal(sum)).setScale(6, RoundingMode.DOWN)
                    .divide(BigDecimal.valueOf(count), RoundingMode.DOWN);
            result.append(String.format("| Минимальное значение = %d%n", min));
            result.append(String.format("| Максимальное значение = %d%n", max));
            result.append(String.format("| Среднее арифметическое значение = %s%n", average));
            result.append(String.format("| Сумма всех элементов = %s%n", sum));
        }

        return result.toString();
    }
}

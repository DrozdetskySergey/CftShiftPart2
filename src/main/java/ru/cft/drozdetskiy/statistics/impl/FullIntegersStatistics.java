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
    private BigInteger minInteger;
    /**
     * Максимальное значение.
     */
    private BigInteger maxInteger;
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
        BigInteger integer = new BigInteger(value);
        minInteger = minInteger == null ? integer : minInteger.min(integer);
        maxInteger = maxInteger == null ? integer : maxInteger.max(integer);
        sum = sum.add(integer);
        count++;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("Количество целых чисел = %d%n", count));

        if (count > 0) {
            BigDecimal average = (new BigDecimal(sum)).setScale(6, RoundingMode.DOWN)
                    .divide(BigDecimal.valueOf(count), RoundingMode.DOWN);
            result.append(String.format("| Минимальное значение = %d%n", minInteger));
            result.append(String.format("| Максимальное значение = %d%n", maxInteger));
            result.append(String.format("| Среднее арифметическое значение = %s%n", average));
            result.append(String.format("| Сумма всех целых чисел = %s%n", sum));
        }

        return result.toString();
    }
}

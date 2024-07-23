package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

import java.math.BigInteger;

class FullLongsStatistics implements Statistics<Long> {

    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;
    private BigInteger sum = BigInteger.ZERO;
    private int count;

    @Override
    public void include(Long value) {
        long number = value; // only one unboxing

        if (number < min) {
            min = number;
        }

        if (number > max) {
            max = number;
        }

        sum = sum.add(BigInteger.valueOf(value));
        count++;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("Количество целых чисел = %d%n", count));

        if (count > 0) {
            long average = (sum.divide(BigInteger.valueOf(count))).longValue();
            result.append(String.format("| Минимальное значение = %d%n", min));
            result.append(String.format("| Максимальное значение = %d%n", max));
            result.append(String.format("| Среднее арифметическое значение = %d%n", average));
            result.append(String.format("| Сумма всех элементов = %s%n", sum.toString()));
        }

        return result.toString();
    }
}

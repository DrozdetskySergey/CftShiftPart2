package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

import java.math.BigInteger;

class FullLongsStatistics implements Statistics<Long> {

    private long min;
    private long max;
    private BigInteger sum;
    private int count;

    public FullLongsStatistics() {
        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
        sum = BigInteger.ZERO;
    }

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
    public String getInfo() {
        String result = String.format("Количество элементов типа Long = %d%n", count);

        if (count > 0) {
            long average = (sum.divide(BigInteger.valueOf(count))).longValue();
            result += String.format("| Минимальное значение = %d%n", min) +
                    String.format("| Максимальное значение = %d%n", max) +
                    String.format("| Среднее арифметическое значение = %d%n", average) +
                    String.format("| Сумма всех элементов = %s%n", sum.toString());
        }

        return result;
    }
}

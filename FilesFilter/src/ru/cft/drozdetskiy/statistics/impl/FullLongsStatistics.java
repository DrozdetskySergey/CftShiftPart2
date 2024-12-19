package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class FullLongsStatistics implements Statistics<Long> {

    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;
    private BigDecimal sum = BigDecimal.ZERO;
    private long count = 0;

    @Override
    public void include(Long value) {
        long number = value;

        if (number < min) {
            min = number;
        }

        if (number > max) {
            max = number;
        }

        sum = sum.add(BigDecimal.valueOf(number));
        count++;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("Количество целых чисел = %d%n", count));

        if (count > 0) {
            BigDecimal average = sum.setScale(6, RoundingMode.DOWN)
                    .divide(BigDecimal.valueOf(count), RoundingMode.DOWN);
            sum = sum.setScale(0, RoundingMode.DOWN);
            result.append(String.format("| Минимальное значение = %d%n", min));
            result.append(String.format("| Максимальное значение = %d%n", max));
            result.append(String.format("| Среднее арифметическое значение = %s%n", average));
            result.append(String.format("| Сумма всех элементов = %s%n", sum));
        }

        return result.toString();
    }
}

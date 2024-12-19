package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class FullDoublesStatistics implements Statistics<Double> {

    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;
    private BigDecimal sum = BigDecimal.ZERO;
    private long count = 0;

    @Override
    public void include(Double value) {
        double number = value;

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
        StringBuilder result = new StringBuilder(String.format("Количество вещественных чисел = %d%n", count));

        if (count > 0) {
            sum = sum.setScale(16, RoundingMode.DOWN);
            BigDecimal average = sum.divide(BigDecimal.valueOf(count), RoundingMode.DOWN);
            result.append(String.format("| Минимальное значение = %e%n", min));
            result.append(String.format("| Максимальное значение = %e%n", max));
            result.append(String.format("| Среднее арифметическое значение = %s%n", average));
            result.append(String.format("| Сумма всех элементов = %s%n", sum));
        }

        return result.toString();
    }
}

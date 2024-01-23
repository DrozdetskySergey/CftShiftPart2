package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

class FullDoublesStatistics implements Statistics<Double> {

    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;
    private BigDecimal sum = BigDecimal.ZERO;
    private int count;

    @Override
    public void include(Double value) {
        double number = value; // only one unboxing

        if (number < min) {
            min = number;
        }

        if (number > max) {
            max = number;
        }

        sum = sum.add(BigDecimal.valueOf(value));
        count++;
    }

    @Override
    public String getInfo() {
        StringBuilder result = new StringBuilder(String.format("Количество элементов типа Double = %d%n", count));

        if (count > 0) {
            Double average = (sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_EVEN)).doubleValue();
            result.append(String.format("| Минимальное значение = %f%n", min));
            result.append(String.format("| Максимальное значение = %f%n", max));
            result.append(String.format("| Среднее арифметическое значение = %f%n", average));
            result.append(String.format("| Сумма всех элементов = %s%n", sum.toString()));
        }

        return result.toString();
    }
}

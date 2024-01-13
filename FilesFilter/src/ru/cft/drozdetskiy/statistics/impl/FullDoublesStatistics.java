package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

class FullDoublesStatistics implements Statistics<Double> {

    private double min;
    private double max;
    private BigDecimal sum;
    private int count;

    public FullDoublesStatistics() {
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;
        sum = BigDecimal.ZERO;
    }

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
        String result = String.format("Количество элементов типа Double = %d%n", count);

        if (count > 0) {
            Double average = (sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_EVEN)).doubleValue();
            sum = sum.setScale(8, RoundingMode.HALF_EVEN);
            result += String.format("| Минимальное значение = %.8f%n", min) +
                    String.format("| Максимальное значение = %.8f%n", max) +
                    String.format("| Среднее арифметическое значение = %.8f%n", average) +
                    String.format("| Сумма всех элементов = %s%n", sum.toString());
        }

        return result;
    }
}

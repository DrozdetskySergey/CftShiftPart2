package ru.cft.drozdetskiy.statistics.impl;

import ru.cft.drozdetskiy.statistics.Statistics;

class FullDoublesStatistics implements Statistics<Double> {

    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;
    private double sum = 0;
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

        sum += number;
        count++;
    }

    @Override
    public String getInfo() {
        StringBuilder result = new StringBuilder(String.format("Количество вещественных чисел = %d%n", count));

        if (count > 0) {
            double average = sum / count;
            result.append(String.format("| Минимальное значение = %e%n", min));
            result.append(String.format("| Максимальное значение = %e%n", max));
            result.append(String.format("| Среднее арифметическое значение = %e%n", average));
            result.append(String.format("| Сумма всех элементов = %e%n", sum));
        }

        return result.toString();
    }
}

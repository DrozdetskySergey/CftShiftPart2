package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Полная статистика для значения типа {@linkplain ContentType#FLOAT FLOAT} переданного в формате строки.
 */
final class FullFloatsStatistics implements Statistics {

    /**
     * Минимальное значение.
     */
    private BigDecimal minDecimal;
    /**
     * Максимальное значение.
     */
    private BigDecimal maxDecimal;
    /**
     * Сумма.
     */
    private BigDecimal sum = BigDecimal.ZERO;
    /**
     * Флаг. Сигнализирует о том, что приходящие значения не имели крупного масштаба (scale).
     */
    private boolean isScaleWithinLimit = true;
    /**
     * Количество.
     */
    private long count;

    @Override
    public void include(String value) {
        BigDecimal decimal = new BigDecimal(value);
        minDecimal = minDecimal == null ? decimal : minDecimal.min(decimal);
        maxDecimal = maxDecimal == null ? decimal : maxDecimal.max(decimal);

        final int scaleLimit = 100000;
        isScaleWithinLimit = isScaleWithinLimit && Math.abs(decimal.scale()) < scaleLimit;

        if (isScaleWithinLimit) {
            sum = sum.add(decimal);
        }

        count++;
    }

    @Override
    public String toString() {
        final String message = "Сбой. Переполнение поддерживаемого диапазона.";
        StringBuilder result = new StringBuilder(String.format("Количество вещественных чисел = %d%n", count));

        if (count > 0) {
            BigDecimal average = sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_EVEN);
            result.append(String.format("| Минимальное значение = %s%n", minDecimal));
            result.append(String.format("| Максимальное значение = %s%n", maxDecimal));
            result.append(String.format("| Среднее арифметическое значение = %s%n", isScaleWithinLimit ? average : message));
            result.append(String.format("| Сумма всех вещественных чисел = %s%n", isScaleWithinLimit ? sum : message));
        }

        return result.toString();
    }
}

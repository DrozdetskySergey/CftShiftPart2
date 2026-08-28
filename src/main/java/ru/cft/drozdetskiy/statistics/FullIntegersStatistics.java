package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Полная статистика для значения типа {@linkplain ContentType#INTEGER INTEGER} переданного в формате строки.
 */
final class FullIntegersStatistics extends Statistics {

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
    private BigInteger sum;

    /**
     * Полная статистика для значения типа {@linkplain ContentType#INTEGER INTEGER} переданного в формате строки.
     */
    public FullIntegersStatistics() {
        super(ContentType.INTEGER);

        sum = BigInteger.ZERO;
    }

    @Override
    public void include(String value) {
        BigInteger integer = new BigInteger(value);
        minInteger = minInteger == null ? integer : minInteger.min(integer);
        maxInteger = maxInteger == null ? integer : maxInteger.max(integer);
        sum = sum.add(integer);
        count++;
    }

    @Override
    public String getAdditionalInfo() {
        StringBuilder result = new StringBuilder();

        if (count > 0) {
            BigDecimal average = (new BigDecimal(sum)).setScale(6, RoundingMode.HALF_EVEN)
                    .divide(BigDecimal.valueOf(count), RoundingMode.HALF_EVEN).stripTrailingZeros();
            result.append(String.format("Минимальное целое число = %s%n", minInteger));
            result.append(String.format("Максимальное целое число = %s%n", maxInteger));
            result.append(String.format("Среднее арифметическое значение = %s%n", average));
            result.append(String.format("Сумма всех целых чисел = %s%n", sum));
        }

        return result.toString();
    }
}

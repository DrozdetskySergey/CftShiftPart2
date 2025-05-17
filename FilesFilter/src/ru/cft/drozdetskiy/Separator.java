package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactories;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static ru.cft.drozdetskiy.ContentType.*;

/**
 * Распределяет строки в зависимости от типа содержимого {@link ContentType} в объекты
 * интерфейса {@link Appendable}, переданные в конструктор. При этом собирается статистика.
 */
final class Separator {

    /**
     * Словарь с объектами интерфейса {@link Appendable} в соответствии с типом {@link ContentType}
     */
    private final Map<ContentType, Appendable> writers;

    /**
     * Распределяет строки в зависимости от типа содержимого {@link ContentType} в объекты
     * интерфейса {@link Appendable}
     *
     * @param writers словарь {@link Map} с объектами интерфейса {@link Appendable}
     *                в соответствии с типом {@link ContentType}
     */
    public Separator(Map<ContentType, Appendable> writers) {
        this.writers = writers;
    }

    /**
     * Обрабатывает строки из объекта интерфейса {@link Iterator}.
     * В зависимости от типа содержимого {@link ContentType} строка отправляется в один из объектов
     * интерфейса {@link Appendable} переданных в конструктор.
     * При этом собирается статистика требуемого типа {@link StatisticsType}.
     *
     * @param iterator       объекта интерфейса {@link Iterator}
     * @param statisticsType требуемый тип статистики {@link StatisticsType}
     * @return неизменяемый словарь {@link Map} с собранной статистикой в соответствии с типом {@link ContentType}
     * @throws IOException если один из объектов интерфейса {@link Appendable} бросает IOException.
     */
    public Map<ContentType, Statistics<?>> handleStrings(Iterator<String> iterator, StatisticsType statisticsType) throws IOException {
        StatisticsFactory factory = StatisticsFactories.get(statisticsType);
        Statistics<Long> longStatistics = factory.createForLong();
        Statistics<Double> doubleStatistics = factory.createForDouble();
        Statistics<String> stringStatistics = factory.createForString();

        while (iterator.hasNext()) {
            String next = iterator.next();
            ContentType type = getContentType(next);

            if (type == LONG) {
                if (isInRange(next)) {
                    longStatistics.include(Long.valueOf(next));
                } else {
                    type = DOUBLE;
                }
            }

            if (type == DOUBLE) {
                double number = Double.parseDouble(next);

                if (Double.isFinite(number)) {
                    doubleStatistics.include(number);
                } else {
                    type = STRING;
                }
            }

            if (type == STRING) {
                stringStatistics.include(next);
            }

            writers.get(type).append(next).append(System.lineSeparator());
        }

        return Map.of(LONG, longStatistics, DOUBLE, doubleStatistics, STRING, stringStatistics);
    }

    /**
     * Определяет тип содержимого строки методом исключения.
     * Сначала проверяет, что это целое число. Если нет, то проверяет, что это вещественное число.
     * Если нет, тогда это простая строка.
     * У целого числа первый символ кроме цифры может быть '+' или '-', остальные символы должны быть цифры.
     * У вещественного числа начало как у целого, а потом должен встретиться один из символов:
     * '.', 'e', 'E' при определённых условиях. Возможно сочетание символа '.' и после него
     * одного из символов: 'e', 'E'.
     *
     * @param string проверяемая строка.
     * @return тип содержимого строки {@link ContentType}
     */
    private ContentType getContentType(String string) {
        if (string.isEmpty()) {
            return STRING;
        }

        char[] symbols = string.toCharArray();
        int firstIndex = symbols[0] == '+' || symbols[0] == '-' ? 1 : 0;
        ContentType result = firstIndex == symbols.length ? STRING : LONG;

        for (int i = firstIndex; result != STRING && i < symbols.length; i++) {
            if (symbols[i] == '.' && result == LONG) {
                result = DOUBLE;
            } else if ((symbols[i] == 'e' || symbols[i] == 'E')
                    && ((result == LONG && firstIndex < i) || (result == DOUBLE && firstIndex + 1 < i))) {
                result = isIntegerNumeric(string.substring(i + 1)) ? DOUBLE : STRING;
                break;
            } else if (symbols[i] < '0' || symbols[i] > '9') {
                result = STRING;
            }
        }

        return result;
    }

    /**
     * ВНИМАНИЕ: содержимое проверяемой строки должно быть целым числом!
     * Проверяет, что это целое число попадает в диапазон -9223372036854775808..9223372036854775807.
     *
     * @param string проверяемая строка.
     * @return true если попадает в диапазон.
     */
    private boolean isInRange(String string) {
        boolean result = true;

        if (string.length() > 18) {
            char[] symbols = string.toCharArray();
            int firstIndex = 0;

            for (char c = symbols[firstIndex]; c == '0' || c == '+' || c == '-'; c = symbols[firstIndex]) {
                firstIndex++;
            }

            if (string.length() > firstIndex + 19) {
                result = false;
            } else if (string.length() == firstIndex + 19 && symbols[firstIndex] == '9') {
                String digits = string.substring(firstIndex);

                if ((symbols[0] == '-' && digits.compareTo("9223372036854775808") > 0) ||
                        (symbols[0] != '-' && digits.compareTo("9223372036854775807") > 0)) {
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * Проверяет содержимое строки это целое число или нет.
     * Т.е. первый символ кроме цифры может быть '+' или '-', остальные символы могут быть только цифры.
     *
     * @param string проверяемая строка.
     * @return true если строка это целое число.
     */
    private boolean isIntegerNumeric(String string) {
        if (string.isEmpty()) {
            return false;
        }

        char[] symbols = string.toCharArray();
        int firstIndex = symbols[0] == '+' || symbols[0] == '-' ? 1 : 0;
        boolean result = firstIndex < symbols.length;

        for (int i = firstIndex; result && i < symbols.length; i++) {
            result = symbols[i] >= '0' && symbols[i] <= '9';
        }

        return result;
    }
}
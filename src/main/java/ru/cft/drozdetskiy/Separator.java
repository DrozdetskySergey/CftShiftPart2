package ru.cft.drozdetskiy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactories;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

import static ru.cft.drozdetskiy.ContentType.*;

/**
 * Распределяет строки в зависимости от {@linkplain  ContentType типа содержимого} по объектам
 * интерфейса {@link Appendable}, переданные в конструктор. При этом собирается статистика требуемого
 * {@linkplain  StatisticsType типа }.
 */
final class Separator {

    private static final Logger LOG = LoggerFactory.getLogger(Separator.class);

    /**
     * Словарь с объектами интерфейса {@link Appendable} в соответствии с {@linkplain  ContentType типом}.
     */
    private final Map<ContentType, Appendable> writers;

    /**
     * Распределяет строки в зависимости от {@linkplain  ContentType типа содержимого} по объектам
     * интерфейса {@link Appendable}.
     *
     * @param writers словарь с объектами интерфейса {@link Appendable}
     *                в соответствии с {@linkplain  ContentType типом}.
     */
    public Separator(Map<ContentType, Appendable> writers) {
        this.writers = new EnumMap<>(writers);
    }

    /**
     * Обрабатывает строки из объекта интерфейса {@link Iterator}.
     * В зависимости от {@linkplain  ContentType типа содержимого} строка отправляется в один из объектов
     * интерфейса {@link Appendable} переданных в конструктор этого класса.
     * При этом собирается и потом отдаётся статистика требуемого {@linkplain  StatisticsType типа}.
     *
     * @param iterator       объект интерфейса {@link Iterator} параметризованный строкой.
     * @param statisticsType требуемый {@linkplain  StatisticsType тип статистики}.
     * @return Неизменяемый словарь {@link Map} с собранной статистикой в соответствии с
     * {@linkplain  ContentType типом содержимого} обработанных строк.
     * @throws IOException           если метод append у объектов интерфейса {@link Appendable} бросает IOException.
     * @throws NumberFormatException если ошибочно определён {@linkplain  ContentType типа содержимого} в строке.
     */
    public Map<ContentType, Statistics<?>> handleStrings(Iterator<String> iterator, StatisticsType statisticsType) throws IOException {
        StatisticsFactory factory = StatisticsFactories.get(statisticsType);
        Statistics<Long> integersStatistics = factory.createForInteger();
        Statistics<Double> floatsStatistics = factory.createForFloat();
        Statistics<String> stringsStatistics = factory.createForString();

        while (iterator.hasNext()) {
            String next = iterator.next();
            ContentType type = getContentType(next);

            if (type == INTEGER) {
                if (isInRange(next)) {
                    integersStatistics.include(Long.valueOf(next));
                } else {
                    type = FLOAT;
                    LOG.trace("Целое число {} не попадает в максимальный диапазон и будет обработано как вещественное.", next);
                }
            }

            if (type == FLOAT) {
                double number = Double.parseDouble(next);

                if (Double.isFinite(number)) {
                    floatsStatistics.include(number);
                } else {
                    type = STRING;
                    LOG.trace("Вещественное число {} является infinite или NaN и будет обработано как строка", next);
                }
            }

            if (type == STRING) {
                stringsStatistics.include(next);
            }

            writers.get(type).append(next).append(System.lineSeparator());
        }

        return Map.of(INTEGER, integersStatistics, FLOAT, floatsStatistics, STRING, stringsStatistics);
    }

    /**
     * Определяет {@linkplain  ContentType тип содержимого} строки методом исключения.
     * Сначала проверяет, что это целое число. Если нет, то проверяет, что это вещественное число.
     * Если нет, тогда это простая строка.
     * У целого числа первый символ, кроме цифры, может быть плюс или минус, а остальные символы должны быть цифрами.
     * У вещественного числа начало как у целого, а потом, при определённых условиях, должен встретиться
     * один из трёх символов: [точка, e, E]. Так же возможно сочетание символа [точка] и ещё
     * одного из двух символов: [e, E].
     *
     * @param string проверяемая строка.
     * @return {@linkplain  ContentType Тип содержимого} строки.
     */
    private ContentType getContentType(String string) {
        if (string.isEmpty()) {
            return STRING;
        }

        char[] symbols = string.toCharArray();
        int firstIndex = symbols[0] == '+' || symbols[0] == '-' ? 1 : 0;
        ContentType result = firstIndex == symbols.length ? STRING : INTEGER;

        for (int i = firstIndex; result != STRING && i < symbols.length; i++) {
            if (symbols[i] == '.' && result == INTEGER && symbols.length > firstIndex + 1) {
                result = FLOAT;
            } else if ((symbols[i] == 'e' || symbols[i] == 'E')
                    && ((result == INTEGER && i > firstIndex) || (result == FLOAT && i > firstIndex + 1))) {
                result = isIntegerNumeric(string.substring(i + 1)) ? FLOAT : STRING;
                break;
            } else if (symbols[i] < '0' || '9' < symbols[i]) {
                result = STRING;
            }
        }

        return result;
    }

    /**
     * ВНИМАНИЕ: {@linkplain  ContentType тип содержимого} проверяемой строки должен быть - целое число!
     * У целого числа первый символ, кроме цифры, может быть плюс или минус, а остальные символы должны быть цифрами.
     * Проверяет, что это целое число попадает в диапазон от -9223372036854775808 до +9223372036854775807.
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
     * Проверяет {@linkplain  ContentType тип содержимого} строки это целое число или нет.
     * У целого числа первый символ, кроме цифры, может быть плюс или минус, а остальные символы должны быть цифрами.
     *
     * @param string проверяемая строка.
     * @return true если содержимое строки это целое число.
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
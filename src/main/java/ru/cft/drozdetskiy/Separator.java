package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

import static ru.cft.drozdetskiy.ContentType.*;

/**
 * Распределяет строки в зависимости от {@linkplain  ContentType типа содержимого} по объектам
 * интерфейса {@link Appendable}, переданные в конструктор. При этом собирается статистика.
 */
final class Separator {

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
     * В зависимости от {@linkplain ContentType типа содержимого} строка отправляется в один из объектов
     * интерфейса {@link Appendable} переданных в конструктор этого класса.
     * При этом собирается статистика и выдаётся в качестве результата.
     *
     * @param iterator          объект интерфейса {@link Iterator} параметризованный строкой.
     * @param statisticsFactory фабрика для создания объектов интерфейса {@link Statistics}.
     * @return Неизменяемый словарь {@link Map} с собранной {@linkplain Statistics статистикой} в соответствии с
     * {@linkplain  ContentType типом содержимого} обработанных строк.
     * @throws IOException если метод append у объектов интерфейса {@link Appendable} бросает IOException.
     */
    public Map<ContentType, Statistics> handleStrings(Iterator<String> iterator, StatisticsFactory statisticsFactory) throws IOException {
        Map<ContentType, Statistics> allStatistics = new EnumMap<>(ContentType.class);
        Arrays.stream(ContentType.values()).forEach(e -> allStatistics.put(e, statisticsFactory.createFor(e)));

        while (iterator.hasNext()) {
            String next = iterator.next();
            ContentType type = classifyContentType(next);
            writers.get(type).append(next).append(System.lineSeparator());
            allStatistics.get(type).include(next);
        }

        return Map.copyOf(allStatistics);
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
    private ContentType classifyContentType(String string) {
        if (string.isEmpty()) {
            return STRING;
        }

        final char[] symbols = string.toCharArray();
        final int firstIndex = symbols[0] == '+' || symbols[0] == '-' ? 1 : 0;
        final int lastIndex = symbols.length - 1;
        ContentType result = firstIndex <= lastIndex ? INTEGER : STRING; // STRING если строка "+" или "-"

        for (int i = firstIndex; result != STRING && i <= lastIndex; i++) {
            if (symbols[i] == '.' && result == INTEGER && firstIndex != lastIndex) {
                result = FLOAT;
            } else if ((symbols[i] == 'e' || symbols[i] == 'E')
                       && ((result == INTEGER && firstIndex < i) || (result == FLOAT && firstIndex + 1 < i))) {
                result = isConvertedToInteger(string.substring(i + 1)) ? FLOAT : STRING;
                break;
            } else if (symbols[i] < '0' || '9' < symbols[i]) {
                result = STRING;
            }
        }

        return result;
    }

    /**
     * Проверяет что содержимое строки можно преобразовать в Integer исключая Integer.MIN_VALUE,
     * то есть это целое число в диапазоне от -2147483647 до +2147483647.
     * У целого числа первый символ, кроме цифры, может быть плюс или минус, а остальные символы должны быть цифрами.
     *
     * @param string проверяемая строка.
     * @return true если содержимое строки можно конвертировать в Integer без Integer.MIN_VALUE.
     */
    private boolean isConvertedToInteger(String string) {
        if (string.isEmpty()) {
            return false;
        }

        final char[] symbols = string.toCharArray();
        int firstIndex = symbols[0] == '+' || symbols[0] == '-' ? 1 : 0;
        final int lastIndex = symbols.length - 1;

        while (firstIndex < lastIndex && symbols[firstIndex] == '0') {
            firstIndex++;
        }

        final int firstToLastIndexDiff = 9; // строка из 10 цифр имеет разницу крайних индексов 9 (последний - первый)
        boolean result = firstIndex <= lastIndex && lastIndex <= firstIndex + firstToLastIndexDiff;

        for (int i = firstIndex; result && i <= lastIndex; i++) {
            result = '0' <= symbols[i] && symbols[i] <= '9';
        }

        if (result && lastIndex == firstIndex + firstToLastIndexDiff) {
            String digits = string.substring(firstIndex);
            result = digits.compareTo("2147483647") <= 0;
        }

        return result;
    }
}
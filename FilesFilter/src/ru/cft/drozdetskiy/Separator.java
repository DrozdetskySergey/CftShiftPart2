package ru.cft.drozdetskiy;

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
 * Распределяет строки в зависимости от типа содержимого {@link ContentType} в объекты
 * интерфейса {@link Appendable}, переданные в конструктор. При этом собирается статистика.
 */
final class Separator {

    /**
     * Сюда распределяется строка {@linkplain #next} если её содержимое это целое число.
     */
    private final Appendable longWriter;
    /**
     * Сюда распределяется строка {@linkplain #next} если её содержимое это вещественное число.
     */
    private final Appendable doubleWriter;
    /**
     * Сюда распределяется строка {@linkplain #next} если её содержимое это простая строка.
     */
    private final Appendable stringWriter;
    /**
     * Статистика по строкам распределенных в {@linkplain #longWriter}
     */
    private Statistics<Long> longStatistics;
    /**
     * Статистика по строкам распределенных в {@linkplain #doubleWriter}
     */
    private Statistics<Double> doubleStatistics;
    /**
     * Статистика по строкам распределенных в {@linkplain #stringWriter}
     */
    private Statistics<String> stringStatistics;
    /**
     * Следующая строка для распределения в один из объектов интерфейса {@link Appendable}
     */
    private String next;

    /**
     * Распределяет строки в зависимости от типа содержимого {@link ContentType} в объекты
     * интерфейса {@link Appendable}
     *
     * @param writers словарь {@link Map} с объектами интерфейса {@link Appendable}
     *                в соответствии с типом {@link ContentType}
     */
    public Separator(Map<ContentType, Appendable> writers) {
        longWriter = writers.get(LONG);
        doubleWriter = writers.get(DOUBLE);
        stringWriter = writers.get(STRING);
    }

    /**
     * Обрабатывает строки из объекта интерфейса {@link Iterator}.
     * В зависимости от типа содержимого {@link ContentType} строка отправляется в один из объектов
     * интерфейса {@link Appendable} переданных в конструктор.
     * При этом собирается статистика требуемого типа {@link StatisticsType}.
     *
     * @param iterator       объекта интерфейса {@link Iterator}
     * @param statisticsType требуемый тип статистики {@link StatisticsType}
     * @return словарь {@link Map} с собранной статистикой в соответствии с типом {@link ContentType}
     * @throws IOException если один из объектов интерфейса {@link Appendable} бросает IOException.
     */
    public Map<ContentType, Statistics<?>> handleStrings(Iterator<String> iterator, StatisticsType statisticsType) throws IOException {
        StatisticsFactory factory = StatisticsFactories.get(statisticsType);
        longStatistics = factory.createForLong();
        doubleStatistics = factory.createForDouble();
        stringStatistics = factory.createForString();

        while (iterator.hasNext()) {
            next = iterator.next();

            switch (getContentType()) {
                case LONG:
                    handleLongContent();
                    break;
                case DOUBLE:
                    handleDoubleContent();
                    break;
                case STRING:
                    handleStringContent();
                    break;
                default:
                    break;
            }
        }

        Map<ContentType, Statistics<?>> allStatistics = new EnumMap<>(ContentType.class);
        allStatistics.put(LONG, longStatistics);
        allStatistics.put(DOUBLE, doubleStatistics);
        allStatistics.put(STRING, stringStatistics);

        return allStatistics;
    }

    /**
     * Обрабатывает ситуацию когда тип содержимого строки {@linkplain #next} это целое число.
     *
     * @throws IOException если какой-то из методов append объекта {@linkplain #longWriter} бросил IOException
     */
    private void handleLongContent() throws IOException {
        longStatistics.include(Long.valueOf(next));
        longWriter.append(next).append(System.lineSeparator());
    }

    /**
     * Обрабатывает ситуацию когда тип содержимого строки {@linkplain #next} это вещественное число.
     *
     * @throws IOException если какой-то из методов append объекта {@linkplain #doubleWriter} бросил IOException
     */
    private void handleDoubleContent() throws IOException {
        double number = Double.parseDouble(next);

        if (Double.isFinite(number)) {
            doubleStatistics.include(number);
            doubleWriter.append(next).append(System.lineSeparator());
        } else {
            handleStringContent();
        }
    }

    /**
     * Обрабатывает ситуацию когда тип содержимого строки {@linkplain #next} это простая строка.
     *
     * @throws IOException если какой-то из методов append объекта {@linkplain #stringWriter} бросил IOException
     */
    private void handleStringContent() throws IOException {
        if (!next.isEmpty()) {
            stringStatistics.include(next);
            stringWriter.append(next).append(System.lineSeparator());
        }
    }

    /**
     * Определяет тип содержимого строки методом исключения. Сначала проверяет, что это целое число.
     * Если нет, то проверяет, что это вещественное число. Если нет, тогда это простая строка.
     * У целого числа первый символ кроме цифры может быть '+' или '-', остальные символы могут быть только цифры.
     * Целое число должно попадать в диапазон -9223372036854775808..9223372036854775807.
     * У вещественного числа начало как у целого, а потом должен встретиться один из символов:
     * '.', 'e', 'E' при определённых условиях. Возможно сочетание символа '.' и после него
     * одного из символов: 'e', 'E'.
     *
     * @return тип содержимого строки {@link ContentType}
     */
    private ContentType getContentType() {
        if (next.isEmpty()) {
            return STRING;
        }

        char[] symbols = next.toCharArray();
        int firstIndex = symbols[0] == '+' || symbols[0] == '-' ? 1 : 0;
        ContentType result = firstIndex == symbols.length ? STRING : LONG;

        for (int i = firstIndex; result != STRING && i < symbols.length; i++) {
            if (symbols[i] == '.' && result == LONG) {
                result = DOUBLE;
            } else if ((symbols[i] == 'e' || symbols[i] == 'E')
                    && ((result == LONG && firstIndex < i) || (result == DOUBLE && firstIndex + 1 < i))) {
                result = isIntegerNumeric(next.substring(i + 1)) ? DOUBLE : STRING;
                break;
            } else if (symbols[i] < '0' || symbols[i] > '9') {
                result = STRING;
            }
        }

        if (result == LONG) {
            if (symbols.length > firstIndex + 19) {
                result = DOUBLE;
            } else if (symbols.length == firstIndex + 19 && symbols[firstIndex] == '9') {
                String digits = firstIndex == 1 ? next.substring(1) : next;

                if ((symbols[0] == '-' && digits.compareTo("9223372036854775808") > 0) ||
                        (symbols[0] != '-' && digits.compareTo("9223372036854775807") > 0)) {
                    result = DOUBLE;
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
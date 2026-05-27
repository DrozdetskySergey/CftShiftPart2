package ru.cft.drozdetskiy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Распределяет строки в зависимости от {@linkplain  ContentType типа содержимого} по объектам
 * интерфейса {@link Appendable}, переданные в конструктор. При этом собирается статистика.
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
     * В зависимости от {@linkplain ContentType типа содержимого} строка отправляется в один из объектов
     * интерфейса {@link Appendable} переданных в конструктор этого класса. При этом собирается статистика
     * и выдаётся в качестве результата. Если тип содержимого был определен неверно, то обработка строк не прерывается,
     * а ошибочно классифицированная строка считается простой строкой.
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
            ContentType type = ContentTypeClassifier.classify(next);

            try {
                allStatistics.get(type).include(next);
            } catch (NumberFormatException e) {
                type = ContentType.STRING;
                allStatistics.get(type).include(next);
                LOG.warn("Некорректно определён тип содержимого строки '{}' как число, автоматически переопределен как простая строка.", next);
            }

            writers.get(type).append(next).append(System.lineSeparator());
        }

        return Map.copyOf(allStatistics);
    }
}
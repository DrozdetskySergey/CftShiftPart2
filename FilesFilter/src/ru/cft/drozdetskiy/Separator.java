package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsFactoryBuilder;
import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ru.cft.drozdetskiy.ContentType.*;

final class Separator {

    private final Appendable longAppender;
    private final Appendable doubleAppender;
    private final Appendable stringAppender;
    Statistics<Long> longsStatistics;
    Statistics<Double> doublesStatistics;
    Statistics<String> stringsStatistics;

    private Separator(Builder builder) {
        if (builder.longAppender == null || builder.doubleAppender == null || builder.stringAppender == null) {
            throw new IllegalArgumentException("Неверное конструирование класса Separator (appender = null).");
        }

        longAppender = builder.longAppender;
        doubleAppender = builder.doubleAppender;
        stringAppender = builder.stringAppender;
    }

    public static class Builder {
        private Appendable longAppender;
        private Appendable doubleAppender;
        private Appendable stringAppender;

        public Builder longAppender(Appendable appender) {
            longAppender = appender;

            return this;
        }

        public Builder doubleAppender(Appendable appender) {
            doubleAppender = appender;

            return this;
        }

        public Builder stringAppender(Appendable appender) {
            stringAppender = appender;

            return this;
        }

        public Separator build() {
            return new Separator(this);
        }
    }

    public List<Statistics<?>> separate(Iterator<String> iterator, StatisticsType statisticsType) throws IOException {
        StatisticsFactory factory = StatisticsFactoryBuilder.build(statisticsType);
        longsStatistics = factory.createForLong();
        doublesStatistics = factory.createForDouble();
        stringsStatistics = factory.createForString();
        List<Statistics<?>> result = new ArrayList<>(3);
        result.add(longsStatistics);
        result.add(doublesStatistics);
        result.add(stringsStatistics);

        while (iterator.hasNext()) {
            String string = iterator.next();

            switch (getContentType(string)) {
                case LONG:
                    handleLongContent(string);
                    break;
                case DOUBLE:
                    handleDoubleContent(string);
                    break;
                case STRING:
                    handleStringContent(string);
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    private void handleLongContent(String string) throws IOException {
        longsStatistics.include(Long.valueOf(string));
        longAppender.append(string).append(System.lineSeparator());
    }

    private void handleDoubleContent(String string) throws IOException {
        double number = Double.parseDouble(string);

        if (Double.isFinite(number)) {
            doublesStatistics.include(number);
            doubleAppender.append(string).append(System.lineSeparator());
        } else {
            handleStringContent(string);
        }
    }

    private void handleStringContent(String string) throws IOException {
        stringsStatistics.include(string);
        stringAppender.append(string).append(System.lineSeparator());
    }

    private ContentType getContentType(String string) {
        if (string.isEmpty()) {
            return EMPTY;
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

        if (result == LONG) {
            if (symbols.length > firstIndex + 19) {
                result = DOUBLE;
            } else if (symbols.length == firstIndex + 19 && symbols[firstIndex] == '9') {
                String digits = firstIndex == 1 ? string.substring(1) : string;

                if ((symbols[0] == '-' && digits.compareTo("9223372036854775808") > 0) ||
                        (symbols[0] != '-' && digits.compareTo("9223372036854775807") > 0)) {
                    result = DOUBLE;
                }
            }
        }

        return result;
    }

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
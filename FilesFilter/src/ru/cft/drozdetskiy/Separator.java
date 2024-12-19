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

    private final LazyWriter longWriter;
    private final LazyWriter doubleWriter;
    private final LazyWriter stringWriter;

    private Separator(Builder builder) {
        if (builder.longWriter == null || builder.doubleWriter == null || builder.stringWriter == null) {
            throw new IllegalArgumentException("Неверное конструирование класса Separator (writer = null).");
        }

        longWriter = builder.longWriter;
        doubleWriter = builder.doubleWriter;
        stringWriter = builder.stringWriter;
    }

    public static class Builder {
        private LazyWriter longWriter;
        private LazyWriter doubleWriter;
        private LazyWriter stringWriter;

        public Builder longWriter(LazyWriter writer) {
            this.longWriter = writer;

            return this;
        }

        public Builder doubleWriter(LazyWriter writer) {
            this.doubleWriter = writer;

            return this;
        }

        public Builder stringWriter(LazyWriter writer) {
            this.stringWriter = writer;

            return this;
        }

        public Separator build() {
            return new Separator(this);
        }
    }

    public List<Statistics<?>> separate(Iterator<String> iterator, StatisticsType statisticsType) throws IOException {
        StatisticsFactory factory = StatisticsFactoryBuilder.build(statisticsType);
        Statistics<Long> longsStatistics = factory.createForLong();
        Statistics<Double> doublesStatistics = factory.createForDouble();
        Statistics<String> stringsStatistics = factory.createForString();
        List<Statistics<?>> result = new ArrayList<>(3);
        result.add(longsStatistics);
        result.add(doublesStatistics);
        result.add(stringsStatistics);

        while (iterator.hasNext()) {
            String string = iterator.next();
            ContentType type = getContentType(string);

            if (type == LONG) {
                longsStatistics.include(Long.valueOf(string));
                longWriter.append(string).append(System.lineSeparator());
            } else if (type == DOUBLE) {
                double number = Double.parseDouble(string);

                if (Double.isFinite(number)) {
                    doublesStatistics.include(number);
                    doubleWriter.append(string).append(System.lineSeparator());
                } else {
                    stringsStatistics.include(string);
                    stringWriter.append(string).append(System.lineSeparator());
                }
            } else if (type == STRING) {
                stringsStatistics.include(string);
                stringWriter.append(string).append(System.lineSeparator());
            }
        }

        return result;
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
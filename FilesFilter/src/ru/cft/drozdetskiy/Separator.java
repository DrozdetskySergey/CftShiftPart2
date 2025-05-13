package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsFactories;
import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static ru.cft.drozdetskiy.ContentType.*;

final class Separator {

    private final Appendable longWriter;
    private final Appendable doubleWriter;
    private final Appendable stringWriter;
    private Statistics<Long> longsStatistics;
    private Statistics<Double> doublesStatistics;
    private Statistics<String> stringsStatistics;
    private String next;

    private Separator(Builder builder) {
        longWriter = Objects.requireNonNull(builder.longWriter, "Separator.longWriter = null");
        doubleWriter = Objects.requireNonNull(builder.doubleWriter, "Separator.doubleWriter = null");
        stringWriter = Objects.requireNonNull(builder.stringWriter, "Separator.stringWriter = null");
    }

    public static class Builder {
        private Appendable longWriter;
        private Appendable doubleWriter;
        private Appendable stringWriter;

        public Builder longWriter(Appendable longWriter) {
            this.longWriter = longWriter;

            return this;
        }

        public Builder doubleWriter(Appendable doubleWriter) {
            this.doubleWriter = doubleWriter;

            return this;
        }

        public Builder stringWriter(Appendable stringWriter) {
            this.stringWriter = stringWriter;

            return this;
        }

        public Separator build() {
            return new Separator(this);
        }
    }

    public Map<ContentType, Statistics<?>> handleStrings(Iterator<String> iterator, StatisticsType statisticsType) throws IOException {
        StatisticsFactory factory = StatisticsFactories.get(statisticsType);
        longsStatistics = factory.createForLong();
        doublesStatistics = factory.createForDouble();
        stringsStatistics = factory.createForString();

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

        return Map.of(LONG, longsStatistics, DOUBLE, doublesStatistics, STRING, stringsStatistics);
    }

    private void handleLongContent() throws IOException {
        longsStatistics.include(Long.valueOf(next));
        longWriter.append(next).append(System.lineSeparator());
    }

    private void handleDoubleContent() throws IOException {
        double number = Double.parseDouble(next);

        if (Double.isFinite(number)) {
            doublesStatistics.include(number);
            doubleWriter.append(next).append(System.lineSeparator());
        } else {
            handleStringContent();
        }
    }

    private void handleStringContent() throws IOException {
        if (!next.isEmpty()) {
            stringsStatistics.include(next);
            stringWriter.append(next).append(System.lineSeparator());
        }
    }

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
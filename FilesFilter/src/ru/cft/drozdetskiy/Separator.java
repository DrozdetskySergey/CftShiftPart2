package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.statistics.Statistics;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsFactoryBuilder;
import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class Separator {

    private final Writer longWriter;
    private final Writer doubleWriter;
    private final Writer stringWriter;

    private Separator(Builder builder) {
        if (builder.longWriter == null || builder.doubleWriter == null || builder.stringWriter == null) {
            throw new IllegalArgumentException("Incorrect Separator class build (Writer == null).");
        }

        this.longWriter = builder.longWriter;
        this.doubleWriter = builder.doubleWriter;
        this.stringWriter = builder.stringWriter;
    }

    public static class Builder {
        private Writer longWriter;
        private Writer doubleWriter;
        private Writer stringWriter;

        public Builder longWriter(Writer writer) {
            this.longWriter = writer;

            return this;
        }

        public Builder doubleWriter(Writer writer) {
            this.doubleWriter = writer;

            return this;
        }

        public Builder stringWriter(Writer writer) {
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

            if (type == ContentType.LONG) {
                longWriter.append(string).append(System.lineSeparator());
                longsStatistics.include(Long.valueOf(string));
            } else if (type == ContentType.DOUBLE) {
                double number = Double.parseDouble(string);

                if (Double.isFinite(number)) {
                    doubleWriter.append(string).append(System.lineSeparator());
                    doublesStatistics.include(number);
                } else {
                    stringWriter.append(string).append(System.lineSeparator());
                    stringsStatistics.include(string);
                }
            } else {
                stringWriter.append(string).append(System.lineSeparator());
                stringsStatistics.include(string);
            }
        }

        return result;
    }

    private ContentType getContentType(String string) {
        ContentType result = ContentType.STRING;

        if (isDecimalSystem(string)) {
            if (isLongInteger(string)) {
                result = ContentType.LONG;
            } else if (isClassicReal(string) || isExponentialNotation(string)) {
                result = ContentType.DOUBLE;
            }
        }

        return result;
    }

    private boolean isDecimalSystem(String string) {
        if (string.isEmpty()) {
            return false;
        }

        for (char c : string.toCharArray()) {
            if ((c < '0' || c > '9') && c != '.' && c != '-' && c != '+' && c != 'e' && c != 'E') {
                return false;
            }
        }

        return true;
    }

    private boolean isLongInteger(String string) {
        boolean result = true;
        char firstSymbol = string.charAt(0);
        int shift = firstSymbol == '-' || firstSymbol == '+' ? 1 : 0;
        int length = string.length() - shift;
        boolean isNeededDeepVerification = false;

        if (length < 1 || length > 19) {
            result = false;
        } else if (length == 19 && string.charAt(shift) == '9') {
            isNeededDeepVerification = true;
        }

        for (int i = shift; (i < length + shift) && result; i++) {
            char symbol = string.charAt(i);

            if (symbol < '0' || symbol > '9') {
                result = false;
                break;
            }
        }

        if (isNeededDeepVerification && result) {
            String digits = firstSymbol == '-' || firstSymbol == '+' ? string.substring(1) : string;

            if ((firstSymbol == '-' && digits.compareTo("9223372036854775808") > 0) ||
                    (firstSymbol != '-' && digits.compareTo("9223372036854775807") > 0)) {
                result = false;
            }
        }

        return result;
    }

    private boolean isClassicReal(String string) {
        boolean result = true;
        char firstSymbol = string.charAt(0);
        int shift = firstSymbol == '-' || firstSymbol == '+' ? 1 : 0;
        int length = string.length() - shift;
        boolean hasPoint = false;

        for (int i = shift; (i < length + shift) && result; i++) {
            char symbol = string.charAt(i);

            if (symbol == '.') {
                if (hasPoint || length == 1) {
                    result = false;
                } else {
                    hasPoint = true;
                }
            } else if (symbol < '0' || symbol > '9') {
                result = false;
            }
        }

        return result;
    }

    private boolean isExponentialNotation(String string) {
        int length = string.length();
        int expIndex = 0;

        for (int i = 1; i < length - 1; i++) {
            char symbol = string.charAt(i);

            if (symbol == 'e' || symbol == 'E') {
                expIndex = i;
                break;
            }
        }

        return expIndex != 0 && isClassicReal(string.substring(0, expIndex)) &&
                isLongInteger(string.substring(expIndex + 1));
    }
}
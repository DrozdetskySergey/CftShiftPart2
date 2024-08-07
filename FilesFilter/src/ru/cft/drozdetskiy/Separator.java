package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.buffer.impl.FastBuffer;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsFactoryBuilder;
import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

class Separator {

    private final Buffer<Long> longBuffer;
    private final Buffer<Double> doubleBuffer;
    private final Buffer<String> stringBuffer;

    public Separator(StatisticsType statisticsType) {
        StatisticsFactory factory = StatisticsFactoryBuilder.build(statisticsType);
        longBuffer = new FastBuffer<>(factory.createForLong());
        doubleBuffer = new FastBuffer<>(factory.createForDouble());
        stringBuffer = new FastBuffer<>(factory.createForString());
    }

    public Map<String, Buffer<?>> separate(Iterator<String> iterator) {
        boolean isAdded = true;

        while (iterator.hasNext() && isAdded) {
            String s = iterator.next();

            if (isDecimalSystem(s)) {
                if (isLongInteger(s)) {
                    isAdded = longBuffer.add(Long.valueOf(s));
                } else if (isClassicReal(s) || isExponentialNotation(s)) {
                    double number = Double.parseDouble(s);
                    isAdded = Double.isFinite(number) ? doubleBuffer.add(number) : stringBuffer.add(s);
                } else {
                    isAdded = stringBuffer.add(s);
                }
            } else {
                isAdded = stringBuffer.add(s);
            }
        }

        if (!isAdded) {
            System.out.println("Не удалось добавить в буфер очередную строку. Фильтрация прервана.");
        }

        Map<String, Buffer<?>> result = new TreeMap<>();
        result.put("integers", longBuffer);
        result.put("floats", doubleBuffer);
        result.put("strings", stringBuffer);

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
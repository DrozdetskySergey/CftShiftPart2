package ru.cft.drozdetskiy;

import org.apache.commons.lang3.math.NumberUtils;
import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.supplier.StringSupplier;

class Filter {

    public static void divide(StringSupplier supplier,
                              Buffer<Long> longBuffer,
                              Buffer<Double> doubleBuffer,
                              Buffer<String> stringBuffer) {
        for (String s = supplier.next(); s != null; s = supplier.next()) {
            if (isDecimalSystem(s)) {
                if (isLongInteger(s)) {
                    longBuffer.add(Long.valueOf(s));
                } else if (NumberUtils.isCreatable(s)) {
                    doubleBuffer.add(Double.valueOf(s));
                } else {
                    stringBuffer.add(s);
                }
            } else {
                stringBuffer.add(s);
            }
        }
    }

    private static boolean isDecimalSystem(String string) {
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

    private static boolean isLongInteger(String string) {
        char firstSymbol = string.charAt(0);
        int i = firstSymbol == '-' || firstSymbol == '+' ? 1 : 0;
        int length = string.length();
        boolean isNeededDeepVerification = false;

        if (length - i < 1 || length - i > 19) {
            return false;
        } else if (length - i == 19 && string.charAt(i) == '9') {
            isNeededDeepVerification = true;
        }

        for (; i < length; i++) {
            char symbol = string.charAt(i);

            if (symbol < '0' || symbol > '9') {
                return false;
            }
        }

        if (isNeededDeepVerification) {
            String digits = firstSymbol == '-' || firstSymbol == '+' ? string.substring(1) : string;

            return (firstSymbol != '-' || digits.compareTo("9223372036854775808") <= 0) &&
                    (firstSymbol == '-' || digits.compareTo("9223372036854775807") <= 0);
        }

        return true;
    }
}

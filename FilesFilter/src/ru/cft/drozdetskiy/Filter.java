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
            if (NumberUtils.isCreatable(s)) {
                if (isLongInteger(s)) {
                    longBuffer.add(Long.valueOf(s));
                } else if (isDecimalSystem(s)) {
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
        for (char c : string.toCharArray()) {
            if ((c < '0' || c > '9') && c != '.' && c != '-' && c != '+' && c != 'e' && c != 'E') {

                return false;
            }
        }

        return true;
    }

    private static boolean isLongInteger(String string) {
        int i = string.charAt(0) == '-' || string.charAt(0) == '+' ? 1 : 0;
        int length = string.length();
        boolean isNeededHardCheck = false;

        if (length - i > 18) {
            if (length - i > 19) {

                return false;
            } else if (string.charAt(i) == '9') {
                isNeededHardCheck = true;
            }
        }

        for (; i < length; i++) {
            char symbol = string.charAt(i);

            if (symbol < '0' || symbol > '9') {

                return false;
            }
        }

        if (isNeededHardCheck) {
            try {
                Long.valueOf(string);
            } catch (NumberFormatException e) {

                return false;
            }
        }

        return true;
    }
}

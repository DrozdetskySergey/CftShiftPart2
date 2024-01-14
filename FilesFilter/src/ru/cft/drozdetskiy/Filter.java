package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.supplier.StringSupplier;

public class Filter {

    public static void divide(StringSupplier supplier,
                              Buffer<Integer> integerBuffer,
                              Buffer<Double> doubleBuffer,
                              Buffer<String> stringBuffer) {
        for (String s = supplier.next(); s != null; s = supplier.next()) {
            FilterType type = FilterType.STRING;
            Double number = null;
            Integer integer = null;

            try {
                number = Double.valueOf(s);
                type = FilterType.DOUBLE;
                integer = Integer.valueOf(s);
                type = FilterType.INTEGER;
            } catch (NumberFormatException ignored) {
                // is not Integer (100%)
            }

            if (type == FilterType.INTEGER) {
                integerBuffer.add(integer);
            } else if (type == FilterType.DOUBLE) {
                doubleBuffer.add(number);
            } else {
                stringBuffer.add(s);
            }
        }
    }
}

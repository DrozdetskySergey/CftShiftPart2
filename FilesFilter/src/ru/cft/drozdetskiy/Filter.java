package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.supplier.StringSupplier;

class Filter {

    public static void divide(StringSupplier supplier,
                              Buffer<Long> longBuffer,
                              Buffer<Double> doubleBuffer,
                              Buffer<String> stringBuffer) {
        for (String s = supplier.next(); s != null; s = supplier.next()) {
            FilterType type = FilterType.STRING;
            Double realNumber = null;
            Long intNumber = null;

            try {
                realNumber = Double.valueOf(s);
                type = FilterType.DOUBLE;
                intNumber = Long.valueOf(s);
                type = FilterType.LONG;
            } catch (NumberFormatException ignored) {
                // is not Long (100%)
            }

            if (type == FilterType.LONG) {
                longBuffer.add(intNumber);
            } else if (type == FilterType.DOUBLE) {
                doubleBuffer.add(realNumber);
            } else {
                stringBuffer.add(s);
            }
        }
    }
}

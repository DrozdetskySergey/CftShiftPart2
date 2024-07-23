package ru.cft.drozdetskiy.buffer;

import ru.cft.drozdetskiy.statistics.Statistics;

public interface Buffer<T> {

    boolean add(T value);

    T get();

    boolean isNotEmpty();

    Statistics<T> getStatistics();
}

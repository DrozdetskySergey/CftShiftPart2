package ru.cft.drozdetskiy.buffer;

public interface Buffer<T> {

    void add(T value);

    boolean isNotEmpty();

    T get();

    String getStatisticsInfo();
}

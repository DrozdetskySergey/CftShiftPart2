package ru.cft.drozdetskiy.buffer;

public interface Buffer<T> {

    void add(T value);

    T get();

    String getStatisticsInfo();
}

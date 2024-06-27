package ru.cft.drozdetskiy.buffer;

public interface Buffer<T> {

    void add(T value);

    T get();

    int getSize();

    String getStatisticsInfo();
}

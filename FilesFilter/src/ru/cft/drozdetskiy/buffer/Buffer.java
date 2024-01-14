package ru.cft.drozdetskiy.buffer;

public interface Buffer<T> {

    void add(T value);

    T removeLast();

    boolean isNotEmpty();

    T getFirst();

    String getStatisticsInfo();
}

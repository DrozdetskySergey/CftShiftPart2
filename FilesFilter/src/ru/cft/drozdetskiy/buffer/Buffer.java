package ru.cft.drozdetskiy.buffer;

public interface Buffer<T> {

    boolean add(T value);

    T get();

    boolean isNotEmpty();

    String getStatisticsInfo();
}

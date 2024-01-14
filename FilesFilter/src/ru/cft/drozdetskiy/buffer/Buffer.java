package ru.cft.drozdetskiy.buffer;

public interface Buffer<T> {

    void add(T value);

    boolean hasNext();

    T next();

    String getStatisticsInfo();
}

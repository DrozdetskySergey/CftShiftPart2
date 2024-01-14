package ru.cft.drozdetskiy.buffer.impl;

import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.util.Deque;
import java.util.LinkedList;

public class FastBuffer<T> implements Buffer<T> {

    private final Statistics<T> statistics;
    private final Deque<T> deque;

    public FastBuffer(Statistics<T> statistics) {
        this.statistics = statistics;
        deque = new LinkedList<>();
    }

    @Override
    public void add(T value) {
        statistics.include(value);
        deque.offer(value);
    }

    @Override
    public T removeLast() {
        return deque.pollLast();
    }

    @Override
    public boolean isNotEmpty() {
        return deque.size() > 0;
    }

    @Override
    public T getFirst() {
        return deque.pollFirst();
    }

    @Override
    public String getStatisticsInfo() {
        return statistics.getInfo();
    }
}

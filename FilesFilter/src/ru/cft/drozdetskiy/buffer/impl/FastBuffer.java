package ru.cft.drozdetskiy.buffer.impl;

import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.util.LinkedList;
import java.util.Queue;

public class FastBuffer<T> implements Buffer<T> {

    private final Statistics<T> statistics;
    private final Queue<T> queue;

    public FastBuffer(Statistics<T> statistics) {
        this.statistics = statistics;
        queue = new LinkedList<>();
    }

    @Override
    public void add(T value) {
        statistics.include(value);
        queue.offer(value);
    }

    @Override
    public boolean isNotEmpty() {
        return queue.size() > 0;
    }

    @Override
    public T get() {
        return queue.poll();
    }

    @Override
    public String getStatisticsInfo() {
        return statistics.getInfo();
    }
}
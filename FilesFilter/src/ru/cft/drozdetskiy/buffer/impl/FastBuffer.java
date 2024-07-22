package ru.cft.drozdetskiy.buffer.impl;

import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.util.LinkedList;
import java.util.Queue;

public class FastBuffer<T> implements Buffer<T> {

    private final Statistics<T> statistics;
    private final Queue<T> queue = new LinkedList<>();

    public FastBuffer(Statistics<T> statistics) {
        this.statistics = statistics;
    }

    @Override
    public boolean add(T value) {
        boolean isAdded = queue.offer(value);

        if (isAdded) {
            statistics.include(value);
        }

        return isAdded;
    }

    @Override
    public T get() {
        return queue.poll();
    }

    @Override
    public boolean isNotEmpty() {
        return queue.size() > 0;
    }

    @Override
    public String getStatisticsInfo() {
        return statistics.getInfo();
    }
}

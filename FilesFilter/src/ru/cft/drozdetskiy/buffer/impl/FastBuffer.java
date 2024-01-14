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

    public void add(T value) {
        statistics.include(value);
        queue.offer(value);
    }

    public boolean hasNext() {
        return queue.size() > 0;
    }

    public T next() {
        return queue.poll();
    }

    public String getStatisticsInfo() {
        return statistics.getInfo();
    }
}

package ru.cft.drozdetskiy.supplier;

public interface Supplier<T> extends AutoCloseable {

    T next();

    boolean hasNext();
}

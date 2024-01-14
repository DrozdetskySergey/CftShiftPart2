package ru.cft.drozdetskiy.supplier;

public interface StringSupplier extends AutoCloseable {

    String next();
}

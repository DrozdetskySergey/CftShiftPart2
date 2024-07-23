package ru.cft.drozdetskiy.supplier.impl;

import ru.cft.drozdetskiy.supplier.Supplier;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileStringSupplier implements Supplier<String> {

    private final List<BufferedReader> readers = new ArrayList<>();
    private int index;
    private String next;

    public FileStringSupplier(List<String> files) {
        for (String s : files) {
            try {
                BufferedReader reader = Files.newBufferedReader(Paths.get(s));
                readers.add(reader);
            } catch (IOException e) {
                System.out.printf("Не удалось открыть файл для чтения: %s%n", s);
            }
        }

        updateNext();
    }

    @Override
    public String next() {
        String result = next;
        updateNext();

        return result;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public void close() {
        for (BufferedReader r : readers) {
            closeBufferedReader(r);
        }
    }

    private void updateNext() {
        next = null;

        while (next == null && readers.size() > 0) {
            try {
                next = readers.get(index).readLine();
            } catch (IOException e) {
                System.out.printf("Сбой чтения из файла %s%n", e.getMessage());
            }

            if (next == null) {
                closeBufferedReader(readers.remove(index));
            } else {
                index++;
            }

            if (index >= readers.size()) {
                index = 0;
            }
        }
    }

    private void closeBufferedReader(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.printf("Сбой закрытия потока чтения из файла %s%n", e.getMessage());
            }
        }
    }
}

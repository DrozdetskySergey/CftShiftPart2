package ru.cft.drozdetskiy.supplier.impl;

import ru.cft.drozdetskiy.supplier.StringSupplier;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileStringSupplier implements StringSupplier {

    private final List<BufferedReader> readers = new ArrayList<>();
    private int index = 0;

    public FileStringSupplier(List<String> files) {
        for (String s : files) {
            try {
                BufferedReader reader = Files.newBufferedReader(Paths.get(s));
                readers.add(reader);
            } catch (IOException e) {
                System.out.printf("Не удалось открыть файл для чтения: %s. %s%n", s, e.getMessage());
            }
        }
    }

    @Override
    public String next() {
        String result = null;

        while (result == null && readers.size() > 0) {
            try {
                result = readers.get(index).readLine();
            } catch (IOException e) {
                System.out.printf("Сбой чтения из потока. %s%n", e.getMessage());
            }

            if (result == null) {
                closeBufferedReader(readers.remove(index));
            } else {
                index++;
            }

            if (index >= readers.size()) {
                index = 0;
            }
        }

        return result;
    }

    @Override
    public void close() {
        for (BufferedReader r : readers) {
            closeBufferedReader(r);
        }
    }

    private void closeBufferedReader(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.printf("Сбой закрытия потока чтения. %s%n", e.getMessage());
            }
        }
    }
}

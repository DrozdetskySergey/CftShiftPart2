package ru.cft.drozdetskiy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class FilesIterator implements Iterator<String>, AutoCloseable {

    private final List<BufferedReader> readers;
    private int index;
    private String next;

    public FilesIterator(List<String> files) throws IOException {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("не заданы файлы (files)");
        }

        readers = new ArrayList<>(files.size());

        for (String s : files) {
            BufferedReader reader = Files.newBufferedReader(Paths.get(s));
            readers.add(reader);
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
                next = null;
                System.err.printf("Сбой чтения из файла, далее игнорируется %s%n", e.getMessage());
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

    private void closeBufferedReader(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.printf("Сбой закрытия файла %s%n", e.getMessage());
            }
        }
    }
}

package ru.cft.drozdetskiy;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class FilesIterator implements Iterator<String>, Closeable {

    private final List<BufferedReader> readers;
    private int index;
    private String next;

    public FilesIterator(List<Path> files) throws IOException {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("не заданы файлы (files)");
        }

        readers = new ArrayList<>(files.size());

        try {
            for (Path p : files) {
                BufferedReader reader = Files.newBufferedReader(p);
                readers.add(reader);
            }
        } catch (Exception e) {
            close();
            throw e;
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
        while (!readers.isEmpty()) {
            closeBufferedReader(readers.remove(0));
        }
    }

    private void updateNext() {
        next = null;

        while (next == null && readers.size() > 0) {
            try {
                next = readers.get(index).readLine();
            } catch (IOException e) {
                System.err.printf("Сбой чтения из файла %s, далее игнорируется.%n", e.getMessage());
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

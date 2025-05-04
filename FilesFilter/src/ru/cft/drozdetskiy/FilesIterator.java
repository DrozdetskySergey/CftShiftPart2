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
import java.util.NoSuchElementException;

/**
 * Итератор по строкам из файлов. Выдаёт по одной строке из каждого файла по очереди.
 * Очерёдность сохраняется как в списке переданных в конструктор файлов.
 */
final class FilesIterator implements Iterator<String>, Closeable {

    /**
     * Список ридеров для чтения строк.
     */
    private final List<BufferedReader> readers;
    /**
     * Индекс ридера в списке, т.е. который читает следующую строку.
     */
    private int index;
    /**
     * Буфер для следующей строки. Актуальная строка которую выдаст итератор через метод next()
     */
    private String next;

    /**
     * Итератор по строкам из файлов. Выдаёт по одной строке из каждого файла по очереди.
     *
     * @param files Список файлов.
     * @throws IllegalArgumentException если передан пустой список фалов.
     * @throws IOException              если ошибка ввода/вывода из файла.
     */
    public FilesIterator(List<Path> files) throws IOException {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("Не заданы файлы (files)");
        }

        readers = new ArrayList<>(files.size());

        try {
            for (Path p : files) {
                BufferedReader reader = Files.newBufferedReader(p);
                readers.add(reader);
            }
        } catch (IOException e) {
            close();
            throw e;
        }

        updateNext();
    }

    @Override
    public String next() {
        if (next == null) {
            throw new NoSuchElementException("Запрашиваемая следующая строка не существует.");
        }

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
            closeReader(readers.remove(readers.size() - 1));
        }
    }

    /**
     * Читает следующую строку из ридера readers.get(index) и записывает её в буфер.
     * Если ридер отдаёт NULL или бросает IOException, тогда он удаляется из списка readers и закрывается.
     * Иначе меняет индекс ридера в списке на следующий или нулевой.
     * IOException перехватывает и отображает сообщение об ошибке.
     */
    private void updateNext() {
        next = null;

        while (next == null && readers.size() > 0) {
            try {
                next = readers.get(index).readLine();
            } catch (IOException e) {
                System.err.printf("Сбой чтения из файла %s, далее игнорируется.%n", e.getMessage());
            }

            if (next == null) {
                closeReader(readers.remove(index));
            } else {
                index++;
            }

            if (index >= readers.size()) {
                index = 0;
            }
        }
    }

    /**
     * Закрывает ридер. Если тот кидает IOException, то перехватывает и отображает сообщение об ошибке.
     *
     * @param reader ридер который требуется закрыть.
     */
    private void closeReader(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.printf("Сбой закрытия файла %s%n", e.getMessage());
            }
        }
    }
}

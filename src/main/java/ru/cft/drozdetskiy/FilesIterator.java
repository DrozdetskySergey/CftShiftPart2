package ru.cft.drozdetskiy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(FilesIterator.class);

    /**
     * Список ридеров для чтения строк.
     */
    private final List<BufferedReader> readers;
    /**
     * Индекс ридера в списке - читающий следующую строку.
     */
    private int index;
    /**
     * Буфер для следующей строки. Актуальная строка, которую выдаст итератор через метод next.
     */
    private String next;
    /**
     * Флаг: объект был закрыт (close) или нет.
     */
    private boolean isClosed;

    /**
     * Итератор по строкам из файлов. Выдаёт по одной строке из каждого файла по очереди.
     *
     * @param files Список файлов.
     * @throws InvalidInputException если передан пустой список фалов.
     * @throws IOException           если сбой при создании ридера для файла.
     */
    public FilesIterator(List<Path> files) throws IOException {
        if (files.isEmpty()) {
            throw new InvalidInputException("не указан(ы) файл(ы).");
        }

        readers = new ArrayList<>(files.size());

        try {
            for (Path p : files) {
                BufferedReader reader = Files.newBufferedReader(p);
                readers.add(reader);
                LOG.debug("Создание ридера для файла {}", p);
            }
        } catch (IOException e) {
            close();
            throw e;
        }

        updateNext();
    }

    @Override
    public String next() {
        throwIllegalStateExceptionIfClosed();

        if (next == null) {
            throw new NoSuchElementException("Запрашиваемая следующая строка не существует.");
        }

        String result = next;
        updateNext();

        return result;
    }

    @Override
    public boolean hasNext() {
        throwIllegalStateExceptionIfClosed();

        return next != null;
    }

    @Override
    public void close() {
        if (isClosed) {
            return;
        }

        isClosed = true;

        while (!readers.isEmpty()) {
            closeReader(readers.remove(0));
        }
    }

    /**
     * Читает следующую строку из буферизованного ридера (который соответствует индексу) и записывает её в буфер.
     * Если ридер отдал строку, то меняет индекс ридера на следующий или нулевой (по кругу).
     * Если ридер отдаёт null или бросает IOException, тогда удаляет его из списка ридеров и закрывает,
     * а строку читает из следующего ридера.
     * IOException перехватывает и отображает сообщение об ошибке.
     */
    private void updateNext() {
        next = null;

        while (next == null && !readers.isEmpty()) {
            try {
                next = readers.get(index).readLine();
            } catch (IOException e) {
                System.err.printf("Сбой чтения из ридера для файла %s, далее он игнорируется.%n", e.getMessage());
                LOG.warn("Сбой чтения из ридера для файла {}", e.getMessage());
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
     * @param reader ридер, который требуется закрыть.
     */
    private void closeReader(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
                LOG.debug("Закрытие ридера для файла.");
            } catch (IOException e) {
                System.err.printf("Сбой закрытия ридера для файла %s%n", e.getMessage());
                LOG.warn("Сбой закрытия ридера для файла {}", e.getMessage());
            }
        }
    }

    /**
     * Бросает IllegalStateException если объект закрыт (close).
     *
     * @throws IllegalStateException если объект закрыт.
     */
    private void throwIllegalStateExceptionIfClosed() {
        if (isClosed) {
            throw new IllegalStateException("Объект FilesIterator находится в неподходящем состоянии для выполняемой операции, уже закрыт (close).");
        }
    }
}

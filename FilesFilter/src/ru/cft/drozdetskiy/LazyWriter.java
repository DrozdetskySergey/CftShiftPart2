package ru.cft.drozdetskiy;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Proxy класс реализующий интерфейсы: Appendable, Closeable.
 * Создаёт проксируемый объект класса {@link BufferedWriter} только тогда,
 * когда первый раз вызывается метод интерфейса {@link Appendable}
 */
final class LazyWriter implements Appendable, Closeable {

    /**
     * Путь к файлу для записи.
     */
    private final Path file;
    /**
     * Массив стандартных опций {@link StandardOpenOption} открытия файла.
     */
    private final OpenOption[] openOptions;
    /**
     * Проксируемый объект реализующий интерфейсы: Appendable, Closeable.
     */
    private Writer writer;

    /**
     * Proxy класс реализующий интерфейсы: Appendable, Closeable.
     * Проксируемый объект класса {@link BufferedWriter} созданный для файла.
     *
     * @param file        путь к файлу.
     * @param openOptions массив стандартных опций {@link StandardOpenOption} открытия файла.
     */
    public LazyWriter(Path file, OpenOption[] openOptions) {
        this.file = file;
        this.openOptions = openOptions;
    }

    @Override
    public Appendable append(CharSequence chars) throws IOException {
        getWriter().append(chars);

        return this;
    }

    @Override
    public Appendable append(CharSequence chars, int start, int end) throws IOException {
        getWriter().append(chars, start, end);

        return this;
    }

    @Override
    public Appendable append(char c) throws IOException {
        getWriter().append(c);

        return this;
    }

    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    /**
     * Создаёт проксируемый объект если он не существует.
     *
     * @return объект класса {@link BufferedWriter} созданный для файла.
     * @throws IOException если ошибка создания или открытия файла.
     */
    private Writer getWriter() throws IOException {
        if (writer == null) {
            writer = Files.newBufferedWriter(file, openOptions);
        }

        return writer;
    }
}

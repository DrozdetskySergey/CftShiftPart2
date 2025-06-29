package ru.cft.drozdetskiy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * Proxy класс реализующий интерфейсы: {@link Appendable}, {@link Closeable}.
 * Проксируемый объект класса {@link BufferedWriter} созданный для файла. Создаёт проксируемый объект только тогда,
 * когда первый раз вызывается метод интерфейса Appendable.
 */
final class LazyWriter implements Appendable, Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(LazyWriter.class);

    /**
     * Путь к файлу для записи.
     */
    private final Path file;
    /**
     * Массив стандартных опций {@link StandardOpenOption} открытия файла.
     */
    private final OpenOption[] openOptions;
    /**
     * Проксируемый объект реализующий интерфейсы: {@link Appendable}, {@link Closeable}.
     */
    private Writer writer;

    /**
     * Proxy класс реализующий интерфейсы: {@link Appendable}, {@link Closeable}.
     * Проксируемый объект класса {@link BufferedWriter} созданный для файла. Создаёт проксируемый объект
     * только тогда, когда первый раз вызывается метод интерфейса Appendable.
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
            LOG.debug("Закрытие врайтера для файла {}", file);
        }
    }

    /**
     * Создаёт проксируемый объект если он не существует.
     *
     * @return Объект класса {@link BufferedWriter} созданный для файла.
     * @throws IOException      если ошибка создания или открытия файла.
     * @throws RuntimeException если заданы не валидные опции открытия файла.
     */
    private Writer getWriter() throws IOException {
        if (writer == null) {
            try {
                writer = Files.newBufferedWriter(file, openOptions);
                LOG.debug("Создание врайтера для файла {}, опции: {}", file, openOptions);
            } catch (IllegalArgumentException | UnsupportedOperationException e) {
                throw new RuntimeException(String.format("Заданы не валидные OpenOptions = %s. %s",
                        Arrays.toString(openOptions), e.getMessage()));
            }
        }

        return writer;
    }
}

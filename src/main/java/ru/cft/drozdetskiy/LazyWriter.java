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
     * Флаг: объект был закрыт (close) или нет.
     */
    private boolean isClosed;

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
        throwIllegalStateExceptionIfClosed();
        getWriter().append(chars);

        return this;
    }

    @Override
    public Appendable append(CharSequence chars, int start, int end) throws IOException {
        throwIllegalStateExceptionIfClosed();
        getWriter().append(chars, start, end);

        return this;
    }

    @Override
    public Appendable append(char c) throws IOException {
        throwIllegalStateExceptionIfClosed();
        getWriter().append(c);

        return this;
    }

    @Override
    public void close() {
        if (isClosed) {
            return;
        }

        isClosed = true;

        if (writer != null) {
            try {
                writer.close();
                LOG.debug("Закрытие врайтера для файла {}", file);
            } catch (IOException e) {
                System.err.printf("Сбой закрытия врайтера для файла %s%n", e.getMessage());
                LOG.warn("Сбой закрытия врайтера для файла {}", e.getMessage());
            }
        }
    }

    /**
     * Создаёт проксируемый объект если он не существует.
     *
     * @return Объект класса {@link BufferedWriter} созданный для файла.
     * @throws IOException если ошибка создания или открытия файла.
     */
    private Writer getWriter() throws IOException {
        if (writer == null) {
            writer = Files.newBufferedWriter(file, openOptions);
            LOG.debug("Создание врайтера для файла {}, опции: {}", file, openOptions);
        }

        return writer;
    }

    /**
     * Бросает IllegalStateException если объект закрыт (close).
     *
     * @throws IllegalStateException если объект закрыт.
     */
    private void throwIllegalStateExceptionIfClosed() {
        if (isClosed) {
            throw new IllegalStateException("Объект BufferedWriter находится в неподходящем состоянии для выполняемой операции, уже закрыт (close).");
        }
    }
}

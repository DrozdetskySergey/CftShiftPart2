package ru.cft.drozdetskiy;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Proxy класс реализующий интерфейсы Appendable, Closeable.
 * Создаёт проксируемый объект класса BufferedWriter только тогда, когда первый раз вызывается метод интерфейса.
 */
final class LazyWriter implements Appendable, Closeable {

    private static final OpenOption[] WRITE_OPTIONS =
            {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};
    private static final OpenOption[] APPEND_OPTIONS =
            {StandardOpenOption.CREATE, StandardOpenOption.APPEND};

    /**
     * Путь к файлу для записи.
     */
    private final Path file;
    /**
     * Массив стандартных опций для открытия файла.
     */
    private final OpenOption[] openOptions;
    /**
     * Ссылка на проксируемый объект реализующий интерфейсы Appendable, Closeable.
     */
    private Writer writer;

    /**
     * Proxy класс реализующий интерфейс Appendable. Проксируемый объект - writer в файл.
     *
     * @param file     путь к файлу.
     * @param isAppend true если способ открытия файла для записи в конец, false если перезапись файла.
     */
    public LazyWriter(Path file, boolean isAppend) {
        this.file = file;
        openOptions = isAppend ? APPEND_OPTIONS : WRITE_OPTIONS;
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
     * Создаёт объект writer если он ещё не существует.
     *
     * @return ссылка на объект writer.
     * @throws IOException если ошибка создания или открытия файла.
     */
    private Writer getWriter() throws IOException {
        if (writer == null) {
            writer = Files.newBufferedWriter(file, openOptions);
        }

        return writer;
    }
}

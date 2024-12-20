package ru.cft.drozdetskiy;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

final class LazyWriter implements Appendable, AutoCloseable {

    private static final OpenOption[] WRITE_OPTIONS =
            {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};
    private static final OpenOption[] APPEND_OPTIONS =
            {StandardOpenOption.CREATE, StandardOpenOption.APPEND};

    private final Path file;
    private final OpenOption[] openOptions;
    private Writer writer;

    public LazyWriter(Path file, boolean isAppend) {
        this.file = file;
        openOptions = isAppend ? APPEND_OPTIONS : WRITE_OPTIONS;
        writer = null;
    }

    @Override
    public Appendable append(CharSequence chars) throws IOException {
        enableWriter();
        writer.append(chars);

        return this;
    }

    @Override
    public Appendable append(CharSequence chars, int start, int end) throws IOException {
        enableWriter();
        writer.append(chars, start, end);

        return this;
    }

    @Override
    public Appendable append(char c) throws IOException {
        enableWriter();
        writer.append(c);

        return this;
    }

    @Override
    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.printf("Сбой закрытия потока записи файла %s%n", e.getMessage());
            }
        }
    }

    private void enableWriter() throws IOException {
        if (writer == null) {
            writer = Files.newBufferedWriter(file, openOptions);
        }
    }
}

package ru.cft.drozdetskiy.writer;

import ru.cft.drozdetskiy.buffer.Buffer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileWriter<T> {

    private static final OpenOption[] WRITE_OPTIONS =
            {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};
    private static final OpenOption[] APPEND_OPTIONS =
            {StandardOpenOption.CREATE, StandardOpenOption.APPEND};

    private final Path file;
    private final Buffer<T> buffer;
    private final OpenOption[] openOptions;

    public FileWriter(Path file, Buffer<T> buffer, boolean isAppend) {
        this.file = file;
        this.buffer = buffer;
        openOptions = isAppend ? APPEND_OPTIONS : WRITE_OPTIONS;
    }

    public void run() {
        try (BufferedWriter bufferWriter = Files.newBufferedWriter(file, openOptions)) {
            for (T value = buffer.get(); value != null; value = buffer.get()) {
                bufferWriter.write(String.format("%s%n", value.toString()));
            }
        } catch (IOException e) {
            System.out.printf("Сбой записи в файл: %s%n", file);
        }
    }
}
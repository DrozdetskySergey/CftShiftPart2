package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.buffer.Buffer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class FileWriterFromBuffer {

    private static final OpenOption[] WRITE_OPTIONS =
            {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};
    private static final OpenOption[] APPEND_OPTIONS =
            {StandardOpenOption.CREATE, StandardOpenOption.APPEND};

    private final Path file;
    private final OpenOption[] openOptions;

    public FileWriterFromBuffer(Path file, boolean isAppend) {
        this.file = file;
        openOptions = isAppend ? APPEND_OPTIONS : WRITE_OPTIONS;
    }

    public <T> void write(Buffer<T> buffer) {
        try (BufferedWriter writer = Files.newBufferedWriter(file, openOptions)) {
            while (buffer.isNotEmpty()) {
                writer.write(String.format("%s%n", buffer.get().toString()));
            }
        } catch (IOException e) {
            System.out.printf("Сбой записи в файл: %s%n", file);
        }
    }
}
package ru.cft.drozdetskiy.writer;

import ru.cft.drozdetskiy.buffer.Buffer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class FileWriterFromBuffer<T> {

    private final String file;
    private final Buffer<T> buffer;

    public FileWriterFromBuffer(String file, Buffer<T> buffer) {
        this.file = file;
        this.buffer = buffer;
    }

    public void run() {
        try (FileWriter writer = new FileWriter(file, true);
             BufferedWriter bufferWriter = new BufferedWriter(writer)) {
            for (T value = buffer.get(); value != null; value = buffer.get()) {
                bufferWriter.write(value.toString());
            }
        } catch (IOException e) {
            System.out.printf("Сбой записи в файл: %s%n", file);
        }
    }
}
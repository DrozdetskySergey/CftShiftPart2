package ru.cft.drozdetskiy.writer;

import ru.cft.drozdetskiy.buffer.Buffer;

public interface WriterFromBuffer {

    <T> void write(Buffer<T> buffer);
}

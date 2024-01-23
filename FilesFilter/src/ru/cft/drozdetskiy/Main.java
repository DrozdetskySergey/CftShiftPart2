package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.args.Parser;
import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.buffer.impl.FastBuffer;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsFactoryBuilder;
import ru.cft.drozdetskiy.supplier.StringSupplier;
import ru.cft.drozdetskiy.supplier.impl.FileStringSupplier;
import ru.cft.drozdetskiy.writer.FileWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser(args);

        try (StringSupplier supplier = new FileStringSupplier(parser.getInputFiles())) {
            StatisticsFactory statisticsFactory = StatisticsFactoryBuilder.buildFactory(parser.getStatisticsType());
            Buffer<Long> longBuffer = new FastBuffer<>(statisticsFactory.createForLong());
            Buffer<Double> doubleBuffer = new FastBuffer<>(statisticsFactory.createForDouble());
            Buffer<String> stringBuffer = new FastBuffer<>(statisticsFactory.createForString());

            Filter.divide(supplier, longBuffer, doubleBuffer, stringBuffer);

            System.out.print(longBuffer.getStatisticsInfo());
            System.out.print(doubleBuffer.getStatisticsInfo());
            System.out.print(stringBuffer.getStatisticsInfo());

            String folder = parser.getFolder() + "\\";

            if (!Paths.get(folder).isAbsolute()) {
                folder = Paths.get("").toAbsolutePath().toString() + "\\" + folder;
            }

            Files.createDirectories(Paths.get(folder));

            writeFileFromBuffer(folder + parser.getPrefix() + "integers.txt", longBuffer, parser.isAppend());
            writeFileFromBuffer(folder + parser.getPrefix() + "floats.txt", doubleBuffer, parser.isAppend());
            writeFileFromBuffer(folder + parser.getPrefix() + "strings.txt", stringBuffer, parser.isAppend());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static <T> void writeFileFromBuffer(String absoluteFileName, Buffer<T> buffer, boolean isAppend) {
        if (buffer.size() > 0) {
            Path path = Paths.get(absoluteFileName);
            FileWriter<T> writer = new FileWriter<>(path, buffer, isAppend);
            writer.write();
        }
    }
}

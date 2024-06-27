package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.args.ArgsParser;
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
        ArgsParser argsParser = new ArgsParser(args);

        if (argsParser.getInputFiles().size() == 0) {
            System.out.println("Не заданы файлы.");
        } else if (argsParser.getUnknownKeys().length() > 0) {
            System.out.printf("Не известные опции: %s%n", argsParser.getUnknownKeys());
        } else {
            filterFiles(argsParser);
        }
    }

    private static void filterFiles(ArgsParser argsParser) {
        try (StringSupplier supplier = new FileStringSupplier(argsParser.getInputFiles())) {
            StatisticsFactory statisticsFactory = StatisticsFactoryBuilder.buildFactory(argsParser.getStatisticsType());
            Buffer<Long> longBuffer = new FastBuffer<>(statisticsFactory.createForLong());
            Buffer<Double> doubleBuffer = new FastBuffer<>(statisticsFactory.createForDouble());
            Buffer<String> stringBuffer = new FastBuffer<>(statisticsFactory.createForString());

            Filter.divide(supplier, longBuffer, doubleBuffer, stringBuffer);

            System.out.print(longBuffer.getStatisticsInfo());
            System.out.print(doubleBuffer.getStatisticsInfo());
            System.out.print(stringBuffer.getStatisticsInfo());

            String folder = argsParser.getFolder() + "\\";

            if (!Paths.get(folder).isAbsolute()) {
                folder = Paths.get("").toAbsolutePath().toString() + "\\" + folder;
            }

            Files.createDirectories(Paths.get(folder));

            String folderAndPrefix = folder + argsParser.getPrefix();

            writeFileFromBuffer(folderAndPrefix + "integers.txt", longBuffer, argsParser.isAppend());
            writeFileFromBuffer(folderAndPrefix + "floats.txt", doubleBuffer, argsParser.isAppend());
            writeFileFromBuffer(folderAndPrefix + "strings.txt", stringBuffer, argsParser.isAppend());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> void writeFileFromBuffer(String absoluteFileName, Buffer<T> buffer, boolean isAppend) {
        if (buffer.getSize() > 0) {
            Path path = Paths.get(absoluteFileName);
            FileWriter<T> writer = new FileWriter<>(path, buffer, isAppend);
            writer.write();
        }
    }
}

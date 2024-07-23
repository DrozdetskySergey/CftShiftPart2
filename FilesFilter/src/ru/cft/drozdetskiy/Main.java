package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.args.ArgsParser;
import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.buffer.impl.FastBuffer;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsFactoryBuilder;
import ru.cft.drozdetskiy.supplier.Supplier;
import ru.cft.drozdetskiy.supplier.impl.FileStringSupplier;
import ru.cft.drozdetskiy.writer.FileWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        final String help = "FilesFilter [options] [files...]\n" +
                "options:\n" +
                "-o <путь>     Путь до файлов с результатом.\n" +
                "-p <префикс>  Префикс имён файлов с результатом.\n" +
                "-a            Режим записи в конец файла.\n" +
                "-s            Краткая статистика.\n" +
                "-f            Полная статистика.\n";

        if (args.length == 0 || args[0].equals("-h")) {
            System.out.print(help);

            return;
        }

        ArgsParser argsParser = new ArgsParser(args);

        if (argsParser.getUnknownKeys().length() > 0) {
            System.out.printf("Не верные опции: %s%n%s", argsParser.getUnknownKeys(), help);
        } else if (argsParser.getInputFiles().size() == 0) {
            System.out.printf("Не заданы файлы.%n%s", help);
        } else {
            filterFiles(argsParser);
        }
    }

    private static void filterFiles(ArgsParser argsParser) {
        try (Supplier<String> supplier = new FileStringSupplier(argsParser.getInputFiles())) {
            StatisticsFactory factory = StatisticsFactoryBuilder.build(argsParser.getStatisticsType());
            Buffer<Long> longBuffer = new FastBuffer<>(factory.createForLong());
            Buffer<Double> doubleBuffer = new FastBuffer<>(factory.createForDouble());
            Buffer<String> stringBuffer = new FastBuffer<>(factory.createForString());

            if (!Filter.divide(supplier, longBuffer, doubleBuffer, stringBuffer)) {
                System.out.println("Буфер не смог обработать строку. Фильтрация прервана.");
            }

            System.out.print(longBuffer.getStatistics());
            System.out.print(doubleBuffer.getStatistics());
            System.out.print(stringBuffer.getStatistics());

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
        if (buffer.isNotEmpty()) {
            Path path = Paths.get(absoluteFileName);
            FileWriter<T> writer = new FileWriter<>(path, isAppend);
            writer.write(buffer);
        }
    }
}

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
            handleFiles(argsParser);
        }
    }

    private static void handleFiles(ArgsParser argsParser) {
        try (Supplier<String> supplier = new FileStringSupplier(argsParser.getInputFiles())) {
            StatisticsFactory factory = StatisticsFactoryBuilder.build(argsParser.getStatisticsType());
            Buffer<Long> longBuffer = new FastBuffer<>(factory.createForLong());
            Buffer<Double> doubleBuffer = new FastBuffer<>(factory.createForDouble());
            Buffer<String> stringBuffer = new FastBuffer<>(factory.createForString());

            if (!Filter.divide(supplier, longBuffer, doubleBuffer, stringBuffer)) {
                System.out.println("Буфер не смог обработать строку. Фильтрация прервана.");
            }

            String folder = prepareFolder(argsParser.getFolder());

            writeFileFromBuffer(Paths.get(folder, argsParser.getPrefix(), "integers.txt"), longBuffer, argsParser.isAppend());
            System.out.print(longBuffer.getStatistics());
            writeFileFromBuffer(Paths.get(folder, argsParser.getPrefix(), "floats.txt"), doubleBuffer, argsParser.isAppend());
            System.out.print(doubleBuffer.getStatistics());
            writeFileFromBuffer(Paths.get(folder, argsParser.getPrefix(), "strings.txt"), stringBuffer, argsParser.isAppend());
            System.out.print(stringBuffer.getStatistics());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String prepareFolder(String folder) {
        StringBuilder result = new StringBuilder(folder);
        result.append("\\");

        try {
            if (!Paths.get(result.toString()).isAbsolute()) {
                result.setLength(0);
                result.append(Paths.get("").toAbsolutePath().toString()).append("\\").append(folder).append("\\");
            }

            Files.createDirectories(Paths.get(result.toString()));
        } catch (Exception e) {
            result.setLength(0);
            System.out.printf("Задана не корректная папка: %s%n", folder);
        }

        return result.toString();
    }

    private static <T> void writeFileFromBuffer(Path path, Buffer<T> buffer, boolean isAppend) {
        if (buffer.isNotEmpty()) {
            FileWriter writer = new FileWriter(path, isAppend);
            writer.write(buffer);
        }
    }
}
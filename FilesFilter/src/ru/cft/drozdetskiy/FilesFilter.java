package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.args.ArgsParser;
import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.buffer.impl.FastBuffer;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsFactoryBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesFilter {

    public static void main(String[] args) {
        final String help = String.format("FilesFilter [options] [files...]%n" +
                "options:%n" +
                "-o <путь>     Путь до файлов с результатом.%n" +
                "-p <префикс>  Префикс имён файлов с результатом.%n" +
                "-a            Режим записи в конец файла.%n" +
                "-s            Краткая статистика.%n" +
                "-f            Полная статистика.%n");

        if (args.length == 0 || args[0].equals("-h")) {
            System.out.print(help);

            return;
        }

        ArgsParser argsParser = new ArgsParser(args);

        if (argsParser.getUnknownOptions().length() > 0) {
            for (char c : argsParser.getUnknownOptions().toCharArray()) {
                System.out.printf("Не верно задана опция: -%c%n", c);
            }

            System.out.print(help);
        } else if (argsParser.getFiles().size() == 0) {
            System.out.printf("Не заданы файлы для фильтрации.%n%s", help);
        } else {
            handleFiles(argsParser);
        }
    }

    private static void handleFiles(ArgsParser argsParser) {
        try (FileStringSupplier supplier = new FileStringSupplier(argsParser.getFiles())) {
            StatisticsFactory factory = StatisticsFactoryBuilder.build(argsParser.getStatisticsType());
            Buffer<Long> longBuffer = new FastBuffer<>(factory.createForLong());
            Buffer<Double> doubleBuffer = new FastBuffer<>(factory.createForDouble());
            Buffer<String> stringBuffer = new FastBuffer<>(factory.createForString());

            Separator.separate(supplier, longBuffer, doubleBuffer, stringBuffer);

            String folder = prepareFolder(argsParser.getFolder());

            writeFileAndPrintStatistics(Paths.get(folder, argsParser.getPrefix() + "integers.txt"), longBuffer, argsParser.isAppend());
            writeFileAndPrintStatistics(Paths.get(folder, argsParser.getPrefix() + "floats.txt"), doubleBuffer, argsParser.isAppend());
            writeFileAndPrintStatistics(Paths.get(folder, argsParser.getPrefix() + "strings.txt"), stringBuffer, argsParser.isAppend());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String prepareFolder(String folder) {
        StringBuilder result = new StringBuilder(folder);

        try {
            if (!Paths.get(folder).isAbsolute()) {
                result.setLength(0);
                result.append(Paths.get("").toAbsolutePath().toString()).append("\\").append(folder);
            }

            Files.createDirectories(Paths.get(result.toString()));
        } catch (Exception e) {
            result.setLength(0);
            System.out.printf("Задана не корректная папка: %s%n", folder);
        }

        return result.toString();
    }

    private static <T> void writeFileAndPrintStatistics(Path path, Buffer<T> buffer, boolean isAppend) {
        if (buffer.isNotEmpty()) {
            FileWriterFromBuffer writer = new FileWriterFromBuffer(path, isAppend);
            writer.write(buffer);
        }

        System.out.print(buffer.getStatistics());
    }
}
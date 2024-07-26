package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.args.ArgsParser;
import ru.cft.drozdetskiy.buffer.Buffer;

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
        try (StringFilesIterator iterator = new StringFilesIterator(argsParser.getFiles())) {
            String folder = prepareFolder(argsParser.getFolder());
            String prefix = argsParser.getPrefix();
            boolean isAppend = argsParser.isAppend();
            Separator separator = new Separator(argsParser.getStatisticsType());

            separator.separate(iterator).forEach((key, value)
                    -> writeFileAndPrintStatistics(Paths.get(folder, prefix + key + ".txt"), value, isAppend));
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
            FileWriterFromBuffer writer = new FileWriterFromBuffer(path);
            writer.write(buffer, isAppend);
        }

        System.out.print(buffer.getStatistics());
    }
}
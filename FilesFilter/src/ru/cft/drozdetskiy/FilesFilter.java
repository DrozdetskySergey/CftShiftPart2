package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.args.ArgumentsDTO;
import ru.cft.drozdetskiy.args.ArgumentsParser;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static ru.cft.drozdetskiy.ContentType.*;

public final class FilesFilter {

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
        } else {
            try {
                handleFiles(ArgumentsParser.parse(args));
            } catch (IllegalArgumentException e) {
                System.out.printf("Не верный аргумент: %s%n", e.getMessage());
                System.out.print(help);
            } catch (RuntimeException e) {
                System.err.printf("Что-то пошло не так! %s%n", e.getMessage());
            } catch (IOException e) {
                System.err.printf("Сбой записи/чтения файла %s%n", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleFiles(ArgumentsDTO dto) throws IOException {
        String folder = prepareFolder(dto.getFolder());
        Path fileWithLongs = Paths.get(folder, dto.getPrefix() + "integers.txt");
        Path fileWithDoubles = Paths.get(folder, dto.getPrefix() + "floats.txt");
        Path fileWithStrings = Paths.get(folder, dto.getPrefix() + "strings.txt");

        try (var longWriter = new LazyWriter(fileWithLongs, dto.isAppend());
             var doubleWriter = new LazyWriter(fileWithDoubles, dto.isAppend());
             var stringWriter = new LazyWriter(fileWithStrings, dto.isAppend());
             var iterator = new FilesIterator(dto.getFiles())) {

            Separator separator = new Separator.Builder()
                    .longAppender(longWriter)
                    .doubleAppender(doubleWriter)
                    .stringAppender(stringWriter)
                    .build();
            Map<ContentType, Statistics<?>> statistics = separator.separate(iterator, dto.getStatisticsType());
            System.out.println(statistics.get(LONG));
            System.out.println(statistics.get(DOUBLE));
            System.out.println(statistics.get(STRING));
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
            throw new IllegalArgumentException("папка " + folder);
        }

        return result.toString();
    }
}
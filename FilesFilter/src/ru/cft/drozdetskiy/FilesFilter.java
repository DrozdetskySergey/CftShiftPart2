package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.args.ArgumentsDTO;
import ru.cft.drozdetskiy.args.ArgumentsParser;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
                Map<ContentType, Statistics<?>> allStatistics = handleFiles(ArgumentsParser.parse(args));
                System.out.println(allStatistics.get(LONG));
                System.out.println(allStatistics.get(DOUBLE));
                System.out.println(allStatistics.get(STRING));
            } catch (IllegalArgumentException e) {
                System.out.printf("Не верно задан аргумент: %s%n%n%s", e.getMessage(), help);
            } catch (RuntimeException e) {
                System.err.printf("Что-то пошло не так! %s%n", e.getMessage());
            } catch (IOException e) {
                System.err.printf("Сбой записи/чтения файла %s%n", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<ContentType, Statistics<?>> handleFiles(ArgumentsDTO dto) throws IOException {
        createDirectory(Path.of(dto.getDirectory()));
        Path longsFile = Path.of(dto.getDirectory(), dto.getPrefix() + "integers.txt");
        Path doublesFile = Path.of(dto.getDirectory(), dto.getPrefix() + "floats.txt");
        Path stringsFile = Path.of(dto.getDirectory(), dto.getPrefix() + "strings.txt");
        throwExceptionIfContains(dto.getFiles(), longsFile);
        throwExceptionIfContains(dto.getFiles(), doublesFile);
        throwExceptionIfContains(dto.getFiles(), stringsFile);
        var longWriter = new LazyWriter(longsFile, dto.isAppend());
        var doubleWriter = new LazyWriter(doublesFile, dto.isAppend());
        var stringWriter = new LazyWriter(stringsFile, dto.isAppend());
        var iterator = new FilesIterator(dto.getFiles());
        Map<ContentType, Statistics<?>> allStatistics;

        try (longWriter; doubleWriter; stringWriter; iterator) {
            Separator separator = new Separator.Builder()
                    .longAppender(longWriter)
                    .doubleAppender(doubleWriter)
                    .stringAppender(stringWriter)
                    .build();
            allStatistics = separator.separate(iterator, dto.getStatisticsType());
        }

        return allStatistics;
    }

    private static void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            throw new IllegalArgumentException("путь " + path.toString());
        }
    }

    private static void throwExceptionIfContains(List<Path> files, Path file) {
        if (files.contains(file)) {
            throw new IllegalArgumentException("имя файла совпадает с именем результата " + file.toString());
        }
    }
}
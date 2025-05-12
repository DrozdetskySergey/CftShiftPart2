package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.args.Arguments;
import ru.cft.drozdetskiy.args.ArgumentsDTO;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import static ru.cft.drozdetskiy.ContentType.*;

public final class FilesFilter {

    public static void main(String[] args) {
        final String help = String.format("FilesFilter [options] [files...]%n" +
                "options:%n" +
                "-o <каталог>  Каталог для файлов с результатом.%n" +
                "-p <префикс>  Префикс имён файлов с результатом.%n" +
                "-a            Режим записи в конец файла.%n" +
                "-s            Краткая статистика.%n" +
                "-f            Полная статистика.%n" +
                "files:%n" +
                "Один или более файлов с абсолютным или относительным путём.%n");

        if (args.length == 0 || args[0].equals("-h")) {
            System.out.print(help);
        } else {
            try {
                Map<ContentType, Statistics<?>> allStatistics = handleFiles(Arguments.parse(args));
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
        createDirectory(Path.of(dto.getDirectory()).toAbsolutePath().normalize());
        Path longsFile = Path.of(dto.getDirectory(), dto.getPrefix() + "integers.txt").toAbsolutePath().normalize();
        Path doublesFile = Path.of(dto.getDirectory(), dto.getPrefix() + "floats.txt").toAbsolutePath().normalize();
        Path stringsFile = Path.of(dto.getDirectory(), dto.getPrefix() + "strings.txt").toAbsolutePath().normalize();
        throwExceptionIfContains(dto.getFiles(), longsFile);
        throwExceptionIfContains(dto.getFiles(), doublesFile);
        throwExceptionIfContains(dto.getFiles(), stringsFile);

        OpenOption[] openOptions = dto.isAppend() ?
                new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND} :
                new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};
        var longWriter = new LazyWriter(longsFile, openOptions);
        var doubleWriter = new LazyWriter(doublesFile, openOptions);
        var stringWriter = new LazyWriter(stringsFile, openOptions);
        var iterator = new FilesIterator(dto.getFiles());
        Map<ContentType, Statistics<?>> allStatistics;

        try (longWriter; doubleWriter; stringWriter; iterator) {
            Separator separator = new Separator.Builder()
                    .longWriter(longWriter)
                    .doubleWriter(doubleWriter)
                    .stringWriter(stringWriter)
                    .build();
            allStatistics = separator.handleAndGetStatistics(iterator, dto.getStatisticsType());
        }

        return allStatistics;
    }

    private static void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            throw new IllegalArgumentException("каталог " + path);
        }
    }

    private static void throwExceptionIfContains(List<Path> files, Path file) {
        if (files.contains(file)) {
            throw new IllegalArgumentException("имя файла совпадает с именем результата " + file);
        }
    }
}
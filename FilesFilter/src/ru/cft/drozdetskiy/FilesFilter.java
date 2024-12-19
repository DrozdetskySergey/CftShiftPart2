package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.args.ArgumentsDTO;
import ru.cft.drozdetskiy.args.ArgumentsParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

            return;
        }

        ArgumentsDTO dto = ArgumentsParser.parse(args);

        if (dto.getWrongArguments().isEmpty()) {
            if (dto.getFiles().isEmpty()) {
                System.out.printf("Не заданы файлы для фильтрации.%n%s", help);
            } else {
                handleFiles(dto);
            }
        } else {
            for (char c : dto.getWrongArguments().toCharArray()) {
                System.out.printf("Не верно задана опция: -%c%n", c);
            }

            System.out.print(help);
        }
    }

    private static void handleFiles(ArgumentsDTO dto) {
        String folder = prepareFolder(dto.getFolder());
        Path fileWithLong = Paths.get(folder, dto.getPrefix() + "integers.txt");
        Path fileWithDouble = Paths.get(folder, dto.getPrefix() + "floats.txt");
        Path fileWithString = Paths.get(folder, dto.getPrefix() + "strings.txt");

        try (LazyWriter longWriter = new LazyWriter(fileWithLong, dto.isAppend());
             LazyWriter doubleWriter = new LazyWriter(fileWithDouble, dto.isAppend());
             LazyWriter stringWriter = new LazyWriter(fileWithString, dto.isAppend());
             StringFilesIterator iterator = new StringFilesIterator(dto.getFiles())) {

            Separator separator = new Separator.Builder()
                    .longWriter(longWriter)
                    .doubleWriter(doubleWriter)
                    .stringWriter(stringWriter)
                    .build();
            separator.separate(iterator, dto.getStatisticsType())
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.out.printf("Сбой записи/чтения файла. %s%n", e.getMessage());
        } catch (Exception e) {
            System.out.printf("Что-то пошло не так! %s%n", e.getMessage());
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
            System.out.printf("Задана не корректный путь: %s%n", folder);
        }

        return result.toString();
    }
}
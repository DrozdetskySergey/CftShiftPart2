package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.args.ArgumentsDTO;
import ru.cft.drozdetskiy.args.ArgumentsParser;
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

        ArgumentsDTO dto = ArgumentsParser.parse(args);

        if (dto.getWrongArguments().length() > 0) {
            for (char c : dto.getWrongArguments().toCharArray()) {
                System.out.printf("Не верно задана опция: -%c%n", c);
            }

            System.out.print(help);
        } else if (dto.getFiles().size() == 0) {
            System.out.printf("Не заданы файлы для фильтрации.%n%s", help);
        } else {
            handleFiles(dto);
        }
    }

    private static void handleFiles(ArgumentsDTO dto) {
        try (StringFilesIterator iterator = new StringFilesIterator(dto.getFiles())) {
            String folder = prepareFolder(dto.getFolder());
            String prefix = dto.getPrefix();
            boolean isAppend = dto.isAppend();
            Separator separator = new Separator(dto.getStatisticsType());

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
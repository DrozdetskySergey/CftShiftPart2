package ru.cft.drozdetskiy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.drozdetskiy.args.Arguments;
import ru.cft.drozdetskiy.args.ArgumentsDTO;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static ru.cft.drozdetskiy.ContentType.*;

/**
 * Утилита фильтрации содержимого файлов. Задача: записать разные типы данных в разные файлы.
 * При запуске в командной строке подается несколько файлов, содержащих целые числа,
 * вещественные числа и строки. В качестве разделителя перевод строки.
 * Строки из файлов читаются по очереди в соответствии с их перечислением в командной строке.
 * Статистика по каждому типу выводится в консоль.
 *
 * @author Дроздецкий Сергей <a href="mailto:bigjunior@mail.ru">email</a>
 */
public final class FilesFilter {

    private static final Logger LOG = LoggerFactory.getLogger(FilesFilter.class);

    public static void main(String[] args) {
        LOG.info("Старт утилиты, args = {}", Arrays.toString(args));
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
                Map<ContentType, Statistics<?>> allStatistics = filterFiles(Arguments.parse(args));
                System.out.println(allStatistics.get(LONG));
                System.out.println(allStatistics.get(DOUBLE));
                System.out.println(allStatistics.get(STRING));
            } catch (NumberFormatException e) {
                System.err.printf("Не верно определён тип содержимого строки. %s%n", e.getMessage());
                LOG.error("Не верно определён тип содержимого строки. {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.printf("Не верно задан аргумент: %s%n%n%s", e.getMessage(), help);
                LOG.info("Не верно задан аргумент: {}", e.getMessage());
            } catch (IOException e) {
                System.err.printf("Ошибка файловой системы. %s%n", e.getMessage());
                LOG.error("Ошибка файловой системы. {}", e.getMessage());
            } catch (Exception e) {
                System.err.printf("Критическая ошибка! %s%n", e.getMessage());
                LOG.error("Критическая ошибка!", e);
            }
        }

        LOG.info("Финиш утилиты.");
    }

    /**
     * Центральный метод всей утилиты FilesFilter.
     *
     * @param dto DTO с вводными данными для утилиты.
     * @return неизменяемый словарь {@link Map} с собранной статистикой в соответствии с типом {@link ContentType}
     * @throws IOException              если произошёл сбой при работе с файловой системой.
     * @throws IllegalArgumentException если пользователь задал не верные данные.
     * @throws NumberFormatException    если не удалось конвертировать строку в определённое число.
     */
    private static Map<ContentType, Statistics<?>> filterFiles(ArgumentsDTO dto) throws IOException {
        createDirectory(Path.of(dto.getDirectory()).toAbsolutePath().normalize());
        Path longsFile = Path.of(dto.getDirectory(), dto.getPrefix() + "integers.txt").toAbsolutePath().normalize();
        Path doublesFile = Path.of(dto.getDirectory(), dto.getPrefix() + "floats.txt").toAbsolutePath().normalize();
        Path stringsFile = Path.of(dto.getDirectory(), dto.getPrefix() + "strings.txt").toAbsolutePath().normalize();
        throwExceptionIfContains(dto.getFiles(), longsFile);
        throwExceptionIfContains(dto.getFiles(), doublesFile);
        throwExceptionIfContains(dto.getFiles(), stringsFile);

        OpenOption[] openOptions = getOpenOptions(dto.isAppend());
        var longWriter = new LazyWriter(longsFile, openOptions);
        var doubleWriter = new LazyWriter(doublesFile, openOptions);
        var stringWriter = new LazyWriter(stringsFile, openOptions);

        Map<ContentType, Appendable> writers = new EnumMap<>(ContentType.class);
        writers.put(LONG, longWriter);
        writers.put(DOUBLE, doubleWriter);
        writers.put(STRING, stringWriter);
        var separator = new Separator(writers);

        Map<ContentType, Statistics<?>> allStatistics;

        try (longWriter; doubleWriter; stringWriter; var iterator = new FilesIterator(dto.getFiles())) {
            allStatistics = separator.handleStrings(iterator, dto.getStatisticsType());
        }

        return allStatistics;
    }

    /**
     * Создаёт каталог по переданному пути.
     *
     * @param path путь для каталога.
     * @throws IllegalArgumentException если не удалось создать каталог.
     */
    private static void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            throw new IllegalArgumentException("каталог " + path);
        }
    }

    /**
     * Бросает IllegalArgumentException если данный список путей к файлам содержит данный путь к файлу.
     *
     * @param files список путей к файлам.
     * @param file  путь к файлу.
     * @throws IllegalArgumentException если список путей содержит путь.
     */
    private static void throwExceptionIfContains(List<Path> files, Path file) {
        if (files.contains(file)) {
            throw new IllegalArgumentException("имя файла совпадает с именем результата " + file);
        }
    }

    /**
     * Создаёт новый массив опций {@link StandardOpenOption} для открытия файла в зависимости от переданного флага.
     *
     * @param isAppend true если нужно открыть файл для записи в конец, false если нужно писать в начало файла.
     * @return массив опций {@link StandardOpenOption}
     */
    private static OpenOption[] getOpenOptions(boolean isAppend) {
        return isAppend ?
                new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND} :
                new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};
    }
}
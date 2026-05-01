package ru.cft.drozdetskiy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.drozdetskiy.args.Arguments;
import ru.cft.drozdetskiy.args.ArgumentsDTO;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static ru.cft.drozdetskiy.ContentType.*;

/**
 * Консольная утилита фильтрации содержимого файлов. Задача: записать разные типы данных в разные файлы.
 * При запуске через аргументы подаются опции и несколько имён файлов. Файлы содержат целые числа,
 * вещественные числа и строки, в качестве разделителя используется перевод строки.
 * Строки читаются по одной из каждого файла по очереди. Порядок сохраняется как их перечисляли в аргументах.
 * Статистика по каждому типу данных выводится в консоль.
 *
 * @author Дроздецкий Сергей, <a href="mailto:drozdetskiy_sergey@mail.ru">drozdetskiy_sergey@mail.ru</a>
 */
public final class FilesFilter {

    private static final Logger LOG = LoggerFactory.getLogger(FilesFilter.class);

    public static void main(String[] args) {
        LOG.info("Запуск утилиты, args = {}", Arrays.toString(args));
        final String help = """
                FilesFilter [опции] [файлы]
                опции:
                -o <каталог>  Каталог для файлов с результатом.
                -p <префикс>  Префикс имён файлов с результатом.
                -a            Режим записи в конец файла.
                -s            Краткая статистика.
                -f            Полная статистика.
                файлы:
                Один или более файлов с абсолютным или относительным путём.
                """;

        if (args.length == 0) {
            System.out.print(help);
        } else {
            try {
                Map<ContentType, Statistics> allStatistics = filterFiles(Arguments.parse(args));
                Arrays.stream(ContentType.values()).map(allStatistics::get).forEach(System.out::println);
                LOG.info("Остановка утилиты.");
            } catch (InvalidInputException e) {
                System.out.printf("Не верно задан аргумент: %s%n%n%s", e.getMessage(), help);
                LOG.info("Не верно задан аргумент: {}", e.getMessage());
            } catch (InvalidPathException e) {
                System.out.printf("Задан не корректный каталог или путь к файлу. %s%n", e.getMessage());
                LOG.info("Задан не корректный каталог или путь к файлу. {}", e.getMessage());
            } catch (IOException e) {
                System.err.printf("Сбой при работе с операций ввода-вывода. %s%n", e.getMessage());
                LOG.error("Сбой при работе с операций ввода-вывода. {}", e.getMessage());
            } catch (Exception e) {
                System.err.printf("Критическая ошибка! %s%n", e.getMessage());
                LOG.error("Критическая ошибка!", e);
            }
        }
    }

    /**
     * Получает подготовленные аргументы через DTO, создаёт нужные объекты и запускает фильтрацию строк из файлов.
     * Возвращает собранную статистику по отфильтрованным строкам.
     *
     * @param dto {@linkplain ArgumentsDTO DTO} с аргументами для утилиты.
     * @return Объект интерфейса {@link Map} с собранной {@linkplain Statistics статистикой} в соответствии с
     * {@linkplain  ContentType типом содержимого} обработанных строк.
     * @throws IOException           если произошёл сбой при работе с файлом.
     * @throws InvalidInputException если пользователь задал не верные данные.
     * @throws InvalidPathException  если заданный пользователем путь нельзя конвертировать в объект интерфейса {@link Path}.
     */
    private static Map<ContentType, Statistics> filterFiles(ArgumentsDTO dto) throws IOException {
        makeDirectory(dto.directory());
        Path integersFile = dto.directory().resolve(dto.prefix() + "integers.txt");
        Path floatsFile = dto.directory().resolve(dto.prefix() + "floats.txt");
        Path stringsFile = dto.directory().resolve(dto.prefix() + "strings.txt");
        throwExceptionIfCollectionsOverlap(List.of(integersFile, floatsFile, stringsFile), dto.files());

        var iterator = new FilesIterator(dto.files());
        var integersWriter = new LazyWriter(integersFile, dto.isAppend());
        var floatsWriter = new LazyWriter(floatsFile, dto.isAppend());
        var stringsWriter = new LazyWriter(stringsFile, dto.isAppend());
        var separator = new Separator(Map.of(INTEGER, integersWriter, FLOAT, floatsWriter, STRING, stringsWriter));

        try (iterator; integersWriter; floatsWriter; stringsWriter) {
            return separator.handleStrings(iterator, dto.statisticsFactory());
        }
    }

    /**
     * В файловой системе создаёт каталог по переданному пути.
     *
     * @param path путь для каталога.
     * @throws InvalidInputException если не удалось создать каталог.
     */
    private static void makeDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            throw new InvalidInputException("каталог %s не удалось создать.", path);
        }
    }

    /**
     * Бросает InvalidInputException если коллекции путей пересекаются (имеют хотя бы один одинаковый путь).
     *
     * @param paths1 первая коллекция путей.
     * @param paths2 вторая коллекция путей.
     * @throws InvalidInputException если коллекции путей пересекаются.
     */
    private static void throwExceptionIfCollectionsOverlap(Collection<Path> paths1, Collection<Path> paths2) {
        for (Path p : paths1) {
            if (paths2.contains(p)) {
                throw new InvalidInputException("совпадающий путь %s", p);
            }
        }
    }
}
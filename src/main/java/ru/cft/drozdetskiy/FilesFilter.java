package ru.cft.drozdetskiy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.drozdetskiy.args.Arguments;
import ru.cft.drozdetskiy.args.ArgumentsDTO;
import ru.cft.drozdetskiy.statistics.Statistics;

import java.io.IOException;
import java.nio.file.*;
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
 * @author Дроздецкий Сергей, <a href="mailto:bigjunior@mail.ru">bigjunior@mail.ru</a>
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
     * Основной метод всей утилиты. Получает подготовленные аргументы через DTO, создаёт нужные объекты
     * и запускает фильтрацию строк из файлов. Возвращает собранную статистику.
     *
     * @param dto DTO с аргументами для утилиты.
     * @return Словарь {@link Map} с собранной статистикой в соответствии с
     * {@linkplain  ContentType типом содержимого} обработанных строк.
     * @throws IOException           если произошёл сбой при работе с файлом.
     * @throws InvalidInputException если пользователь задал не верные данные.
     * @throws InvalidPathException  если заданный пользователем путь нельзя конвертировать в {@linkplain Path}.
     */
    private static Map<ContentType, Statistics> filterFiles(ArgumentsDTO dto) throws IOException {
        createDirectory(dto.directory());
        Path integersFile = dto.directory().resolve(dto.prefix() + "integers.txt");
        Path floatsFile = dto.directory().resolve(dto.prefix() + "floats.txt");
        Path stringsFile = dto.directory().resolve(dto.prefix() + "strings.txt");
        throwInvalidInputExceptionIfContainsAny(dto.files(), List.of(integersFile, floatsFile, stringsFile));

        OpenOption[] openOptions = getOpenOptions(dto.isAppend());
        var integersWriter = new LazyWriter(integersFile, openOptions);
        var floatsWriter = new LazyWriter(floatsFile, openOptions);
        var stringsWriter = new LazyWriter(stringsFile, openOptions);
        var separator = new Separator(Map.of(INTEGER, integersWriter, FLOAT, floatsWriter, STRING, stringsWriter));

        try (integersWriter; floatsWriter; stringsWriter; var iterator = new FilesIterator(dto.files())) {
            return separator.handleStrings(iterator, dto.statisticsFactory());
        }
    }

    /**
     * Создаёт каталог по переданному пути.
     *
     * @param path путь для каталога.
     * @throws InvalidInputException если не удалось создать каталог.
     */
    private static void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            throw new InvalidInputException("каталог %s не удалось создать.", path);
        }
    }

    /**
     * Бросает InvalidInputException если контрольный перечень путей содержит какой-либо из альтернативных путей.
     *
     * @param checklist    контрольный перечень путей.
     * @param alternatives альтернативный перечень путей.
     * @throws InvalidInputException если контрольный перечень путей содержит какой-либо альтернативный путь.
     */
    private static void throwInvalidInputExceptionIfContainsAny(Collection<Path> checklist, Collection<Path> alternatives) {
        for (Path p : alternatives) {
            if (checklist.contains(p)) {
                throw new InvalidInputException("совпадающий путь %s", p);
            }
        }
    }

    /**
     * Создаёт новый массив опций {@link StandardOpenOption} для открытия файла в зависимости от переданного флага.
     *
     * @param isAppend true если нужно открыть файл для записи в конец, false если нужно писать в начало файла.
     * @return Массив опций {@link StandardOpenOption}.
     */
    private static OpenOption[] getOpenOptions(boolean isAppend) {
        return isAppend ?
                new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND} :
                new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};
    }
}
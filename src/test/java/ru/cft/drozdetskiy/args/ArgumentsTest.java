package ru.cft.drozdetskiy.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.cft.drozdetskiy.InvalidInputException;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentsTest {

    @Test
    @DisplayName("Парсинг пустого массива возвращает DTO со значениями по умолчанию")
    void parseEmptyArrayAndReturnDefaultDTO() {
        String[] args = {};

        ArgumentsDTO dto = Arguments.parse(args);

        assertEquals("", dto.prefix());
        assertEquals(Path.of(".").toAbsolutePath().normalize(), dto.directory());
        assertEquals(StatisticsFactory.SIMPLE, dto.statisticsFactory());
        assertFalse(dto.isAppend());
        assertTrue(dto.files().isEmpty());
    }

    @Test
    @DisplayName("Null и пустые строки игнорируются")
    void parseIgnoresNullAndBlankStrings() {
        String[] args = {"-a", null, "   ", "file.txt"};

        ArgumentsDTO dto = Arguments.parse(args);

        assertTrue(dto.isAppend());
        assertEquals(1, dto.files().size());
        assertEquals(Path.of("file.txt").toAbsolutePath().normalize(), dto.files().get(0));
    }

    @Test
    @DisplayName("Одинокий префикс опции вызывает исключение InvalidInputException")
    void parseLoneOptionPrefixThrowsException() {
        String[] args = {"-f", "-"};

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> Arguments.parse(args));

        assertTrue(exception.getMessage().contains("пустая опция"));
    }

    @ParameterizedTest
    @DisplayName("Неизвестные опции вызывают исключение InvalidInputException")
    @ValueSource(strings = {"-x", "-1", "-z"})
    void parseUnknownOptionThrowsException(String arg) {
        String[] args = {arg};

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> Arguments.parse(args));

        assertTrue(exception.getMessage().contains("не известная опция"));
    }

    @ParameterizedTest
    @DisplayName("Опции, требующие аргумент, выбрасывают исключение при его отсутствии")
    @ValueSource(strings = {"-o", "-p"})
    void parseMissingArgumentForOptionThrowsException(String arg) {
        String[] args = {arg};

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> Arguments.parse(args));

        assertTrue(exception.getMessage().contains("требуется аргумент"));
    }

    @Test
    @DisplayName("Слипшиеся опции без аргументов корректно разделяются")
    void parseMergedOptionsAndDecomposedCorrectly() {
        String[] args = {"-asf"};

        ArgumentsDTO dto = Arguments.parse(args);

        assertTrue(dto.isAppend());
        assertEquals(StatisticsFactory.FULL, dto.statisticsFactory());
    }

    @Test
    @DisplayName("Слипшиеся опции с аргументом на конце корректно разделяются")
    void parseMergedOptionsWithArgumentAndDecomposedCorrectly() {
        String[] args = {"-aspprefix"};

        ArgumentsDTO dto = Arguments.parse(args);

        assertTrue(dto.isAppend());
        assertEquals(StatisticsFactory.SIMPLE, dto.statisticsFactory());
        assertEquals("prefix", dto.prefix());
    }

    @Test
    @DisplayName("Многократное указание опции -o объединяет пути (принцип матрешки)")
    void parseMultipleDirectoryOptionsAndResolvesPaths() {
        String[] args = {"-o", "dir1", "-o", "dir2"};

        ArgumentsDTO dto = Arguments.parse(args);
        Path directory = Path.of("dir1").resolve("dir2").toAbsolutePath().normalize();

        assertEquals(directory, dto.directory());
    }

    @Test
    @DisplayName("Многократное указание опции -p конкатенирует префиксы")
    void parseMultiplePrefixOptionsAndConcatenatesPrefixes() {
        String[] args = {"-p", "pre", "-p", "fix"};

        ArgumentsDTO dto = Arguments.parse(args);

        assertEquals("prefix", dto.prefix());
    }

    @Test
    @DisplayName("При указании нескольких опций статистики -s и -f применяется последняя")
    void parseMultipleStatisticsOptions() {
        String[] simpleLast = {"-f", "-s", "-f", "-s"};
        String[] fullLast = {"-s", "-f", "-s", "-f"};

        ArgumentsDTO dtoWithSimpleFactory = Arguments.parse(simpleLast);
        ArgumentsDTO dtoWithFullFactory = Arguments.parse(fullLast);

        assertEquals(StatisticsFactory.SIMPLE, dtoWithSimpleFactory.statisticsFactory());
        assertEquals(StatisticsFactory.FULL, dtoWithFullFactory.statisticsFactory());
    }

    @Test
    @DisplayName("Файлы добавляются в список, пути нормализуются и становятся абсолютными")
    void parseFilesAndConvertedToAbsoluteNormalizedPaths() {
        String[] args = {"../dir/file.txt", "./file2.txt"};

        ArgumentsDTO dto = Arguments.parse(args);
        List<Path> files = dto.files();

        assertEquals(2, files.size());
        assertEquals(Path.of("../dir/file.txt").toAbsolutePath().normalize(), files.get(0));
        assertEquals(Path.of("./file2.txt").toAbsolutePath().normalize(), files.get(1));
    }

    @Test
    @DisplayName("Дублирующиеся пути к файлам игнорируются")
    void parseFilesAndIgnoredDuplicate() {
        String[] args = {"file1.txt", "file2.txt", "dir/../file1.txt", "./file1.txt"};

        ArgumentsDTO dto = Arguments.parse(args);
        List<Path> files = dto.files();

        assertEquals(2, files.size());
        assertEquals(Path.of("file1.txt").toAbsolutePath().normalize(), files.get(0));
        assertEquals(Path.of("file2.txt").toAbsolutePath().normalize(), files.get(1));
    }
}
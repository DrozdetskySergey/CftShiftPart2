package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.InvalidInputException;
import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.cft.drozdetskiy.args.Option.*;

/**
 * Функциональный класс. Специализируется на аргументах для утилиты.
 * Предоставляет статичный метод {@link #parse(String[]) parse}
 */
public final class Arguments {

    private Arguments() {
    }

    /**
     * Парсит массив строк, вычленяет {@linkplain Option опции} и пути до файлов.
     * Создаёт и возвращает DTO с входными данными для утилиты.
     *
     * @param args массив строк.
     * @return {@linkplain ArgumentsDTO DTO} с входными данными для утилиты.
     * @throws InvalidInputException если встречается неизвестная опция.
     * @throws InvalidPathException  если заданный путь нельзя конвертировать в {@linkplain Path}.
     */
    public static ArgumentsDTO parse(String[] args) {
        StringBuilder prefix = new StringBuilder();
        Path directory = null;
        StatisticsType statisticsType = StatisticsType.SIMPLE;
        boolean isAppend = false;
        Set<Path> files = new LinkedHashSet<>();

        List<String> arguments = decomposeArguments(filterAndClean(args));

        for (Iterator<String> iterator = arguments.iterator(); iterator.hasNext(); ) {
            String argument = iterator.next();

            if (isNotOption(argument)) {
                files.add(Path.of(argument).toAbsolutePath().normalize());
            } else {
                char symbol = argument.charAt(1);

                if (symbol == APPEND_FILES.symbol) {
                    isAppend = true;
                } else if (symbol == SIMPLE_STAT.symbol) {
                    statisticsType = StatisticsType.SIMPLE;
                } else if (symbol == FULL_STAT.symbol) {
                    statisticsType = StatisticsType.FULL;
                } else if (symbol == SET_DIRECTORY.symbol && iterator.hasNext()) {
                    directory = directory == null ? Path.of(iterator.next()) : directory.resolve(iterator.next());
                } else if (symbol == SET_PREFIX.symbol && iterator.hasNext()) {
                    prefix.append(iterator.next());
                } else {
                    throw new InvalidInputException("опция %s", argument);
                }
            }
        }

        return new ArgumentsDTO.Builder()
                .prefix(prefix.toString())
                .directory(directory == null ? Path.of(".") : directory)
                .statisticsType(statisticsType)
                .isAppend(isAppend)
                .files(List.copyOf(files))
                .build();
    }

    /**
     * В массиве строк отфильтровывает null и пустые строки, убирает пробелы в начале и в конце каждой стоки.
     * Оставшиеся очищенные строки возвращает в виде списка.
     *
     * @param array массив сток.
     * @return Список отфильтрованных и очищенных строк.
     */
    private static List<String> filterAndClean(String[] array) {
        return Arrays.stream(array)
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(String::strip)
                .collect(Collectors.toList());
    }

    /**
     * Проверят каждый аргумент в списке. Аргумент может быть {@linkplain Option опцией} или нет соответственно.
     * У опции первый символ минус. Опции проверяет на слипание и разделяет на самостоятельные аргументы.
     * Не опции копирует без изменений.
     * Возвращает новый список аргументов без слипшихся опций, при этом не нарушая изначальный порядок.
     *
     * @param arguments список аргументов.
     * @return Список аргументов без слипшихся опций.
     */
    private static List<String> decomposeArguments(List<String> arguments) {
        List<String> result = new ArrayList<>();

        for (String s : arguments) {
            if (isNotOption(s)) {
                result.add(s);
            } else {
                for (int i = 1; i < s.length(); i++) {
                    char symbol = s.charAt(i);
                    result.add("-" + symbol);

                    if ((symbol == SET_DIRECTORY.symbol || symbol == SET_PREFIX.symbol) && (i + 1 < s.length())) {
                        result.add(s.substring(i + 1));
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Проверяет что аргумент не может быть {@linkplain Option опцией}. У опции первый символ минус.
     *
     * @param argument проверяемый аргумент в виде строки.
     * @return true если аргумент не может быть опцией.
     */
    private static boolean isNotOption(String argument) {
        return !argument.startsWith("-");
    }
}

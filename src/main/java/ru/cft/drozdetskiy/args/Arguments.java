package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.InvalidInputException;
import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
     * Создаёт и возвращает {@link ArgumentsDTO DTO} с подготовленными входными данными для утилиты.
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

        for (Iterator<String> iterator = getDecomposedArguments(args).iterator(); iterator.hasNext(); ) {
            String argument = iterator.next();

            if (isNotOption(argument)) {
                files.add(Path.of(argument).toAbsolutePath().normalize());
            } else if (argument.length() < 2) {
                throw new InvalidInputException("опция %s", argument);
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
                .directory((directory == null ? Path.of(".") : directory).toAbsolutePath().normalize())
                .statisticsType(statisticsType)
                .isAppend(isAppend)
                .files(List.copyOf(files))
                .build();
    }

    /**
     * В массиве строк выбрасываются null и пустые строки. Оставшиеся строки являются аргументами утилиты.
     * Аргумент считается опцией, если первый символ это минус. Не опции передаются без изменений,
     * а опции проверяются на слипание и разделяются на самостоятельные аргументы.
     * Возвращается список аргументов без слипшихся опций, при этом изначальный порядок не нарушается.
     *
     * @param args массив сток.
     * @return Список аргументов без слипшихся опций.
     */
    private static List<String> getDecomposedArguments(String[] args) {
        return Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(String::strip)
                .flatMap(s -> {
                    if (s.length() <= 2 || isNotOption(s)) {
                        return Stream.of(s);
                    }

                    List<String> arguments = new ArrayList<>();

                    for (int i = 1; i < s.length(); i++) {
                        char symbol = s.charAt(i);
                        arguments.add("-" + symbol);

                        if ((symbol == SET_DIRECTORY.symbol || symbol == SET_PREFIX.symbol) && (i + 1 < s.length())) {
                            arguments.add(s.substring(i + 1));
                            break;
                        }
                    }

                    return arguments.stream();
                })
                .toList();
    }

    /**
     * Проверяет что аргумент не может быть {@linkplain Option опцией}. У опции первый символ это минус.
     *
     * @param argument проверяемый аргумент в виде строки.
     * @return true если аргумент не может быть опцией.
     */
    private static boolean isNotOption(String argument) {
        return !argument.startsWith("-");
    }
}

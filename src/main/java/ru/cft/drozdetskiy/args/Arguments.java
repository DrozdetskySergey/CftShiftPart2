package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.InvalidInputException;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Функциональный класс. Специализируется на аргументах для утилиты.
 * Реализует статичный метод {@linkplain #parse(String[])}
 */
public final class Arguments {

    private Arguments() {
    }

    /**
     * Парсит массив строк, вычленяет {@linkplain Option опции} и пути до файлов.
     * Возвращает в качестве результата {@linkplain ArgumentsDTO DTO} с подготовленными входными данными для утилиты.
     *
     * @param args массив строк.
     * @return {@linkplain ArgumentsDTO DTO} с входными данными для утилиты.
     * @throws InvalidInputException если встречается неизвестная опция.
     * @throws InvalidPathException  если заданный путь нельзя конвертировать в объект интерфейса {@link Path}.
     */
    public static ArgumentsDTO parse(String[] args) {
        StringBuilder prefix = new StringBuilder();
        Path directory = null;
        StatisticsFactory statisticsFactory = StatisticsFactory.SIMPLE;
        boolean isAppend = false;
        Set<Path> files = new LinkedHashSet<>();

        for (Iterator<String> iterator = getDecomposedArguments(args).iterator(); iterator.hasNext(); ) {
            String argument = iterator.next();

            if (isNotOption(argument)) {
                files.add(Path.of(argument).toAbsolutePath().normalize());
            } else if (argument.equals("-")) {
                throw new InvalidInputException("опция -");
            } else {
                switch (Option.fromSymbolOrThrow(argument.charAt(1))) {
                    case APPEND_FILES -> isAppend = true;
                    case SIMPLE_STAT -> statisticsFactory = StatisticsFactory.SIMPLE;
                    case FULL_STAT -> statisticsFactory = StatisticsFactory.FULL;
                    case SET_DIRECTORY -> {
                        if (iterator.hasNext()) {
                            directory = directory == null ? Path.of(iterator.next()) : directory.resolve(iterator.next());
                        }
                    }
                    case SET_PREFIX -> {
                        if (iterator.hasNext()) {
                            prefix.append(iterator.next());
                        }
                    }
                }
            }
        }

        return new ArgumentsDTO(
                prefix.toString(),
                (directory == null ? Path.of(".") : directory).toAbsolutePath().normalize(),
                statisticsFactory,
                isAppend,
                List.copyOf(files));
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

                        if ((symbol == Option.SET_DIRECTORY.getSymbol() || symbol == Option.SET_PREFIX.getSymbol()) && (i + 1 < s.length())) {
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

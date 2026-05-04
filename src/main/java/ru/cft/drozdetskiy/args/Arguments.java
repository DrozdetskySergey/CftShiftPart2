package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.InvalidInputException;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

/**
 * Функциональный класс. Специализируется на аргументах для утилиты.
 * Реализует статичный метод {@linkplain #parse(String[])}
 */
public final class Arguments {

    private Arguments() {
    }

    /**
     * Парсит массив строк, вычленяет {@linkplain Option опции}, аргументы опций и операнды (пути до файлов).
     * Возвращает в качестве результата {@linkplain ArgumentsDTO DTO} с аргументами для утилиты.
     *
     * @param args массив строк.
     * @return {@linkplain ArgumentsDTO DTO} с аргументами для утилиты.
     * @throws InvalidInputException если встречается неизвестная или пустая опция.
     * @throws InvalidPathException  если заданный путь нельзя конвертировать в объект интерфейса {@link Path}.
     */
    public static ArgumentsDTO parse(String[] args) {
        StringBuilder prefix = new StringBuilder();
        Path directory = null;
        StatisticsFactory statisticsFactory = StatisticsFactory.SIMPLE;
        boolean isAppend = false;
        Set<Path> files = new LinkedHashSet<>();
        Iterator<String> iterator = getDecomposedArguments(args).iterator();

        while (iterator.hasNext()) {
            String argument = iterator.next();

            if (Option.isNotOption(argument)) {
                files.add(Path.of(argument).toAbsolutePath().normalize());
                continue;
            }

            Option option = Option.findByString(argument)
                    .orElseThrow(() -> new InvalidInputException("не известная опция %s", argument));

            if (option.hasArgument() && !iterator.hasNext()) {
                throw new InvalidInputException("для опции %s требуется аргумент", argument);
            }

            switch (option) {
                case APPEND_FILES -> isAppend = true;
                case SIMPLE_STAT -> statisticsFactory = StatisticsFactory.SIMPLE;
                case FULL_STAT -> statisticsFactory = StatisticsFactory.FULL;
                case SET_DIRECTORY ->
                        directory = directory == null ? Path.of(iterator.next()) : directory.resolve(iterator.next());
                case SET_PREFIX -> prefix.append(iterator.next());
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
     * В массиве строк выбрасывают null и пустые строки. Убирает пробелы в начале и конце строк. Оставшиеся строки
     * считаются аргументами: {@linkplain Option опции}, аргументы опций и операнды (пути до файлов).
     * Опции проверяются на слипание и разделяются на одиночные опции и их аргументы. Не опции остаются без изменений.
     * Возвращается список аргументов без слипшихся опций, при этом изначальный порядок не нарушается.
     *
     * @param args массив сток.
     * @return Список аргументов без слипшихся опций.
     * @throws InvalidInputException если встречается неизвестная или пустая опция.
     */
    private static List<String> getDecomposedArguments(String[] args) {
        return Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(String::strip)
                .flatMap(s -> Option.decompose(s).stream())
                .toList();
    }
}

package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.InvalidInputException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Опции программы: {@linkplain #SET_DIRECTORY}, {@linkplain #SET_PREFIX}, {@linkplain #APPEND_FILES},
 * {@linkplain #SIMPLE_STAT}, {@linkplain #FULL_STAT}
 * Опция либо требует аргумент (option argument), либо нет.
 * Опция обязательно начинается с {@linkplain #OPTION_PREFIX}.
 */
enum Option {

    /**
     * Задать каталог для файлов с результатом. Требуется аргумент. Синтаксис: -o [каталог]
     */
    SET_DIRECTORY('o', true),
    /**
     * Задать префикс для имён файлов с результатом. Требуется аргумент. Синтаксис: -p [префикс]
     */
    SET_PREFIX('p', true),
    /**
     * Включить режим записи в конец файла. Синтаксис: -a
     */
    APPEND_FILES('a', false),
    /**
     * Собирать краткую статистику. Синтаксис: -s
     */
    SIMPLE_STAT('s', false),
    /**
     * Собирать полную статистику. Синтаксис: -f
     */
    FULL_STAT('f', false);

    /**
     * Словарь опций в соответствие с символом.
     */
    private static final Map<Character, Option> OPTIONS =
            Arrays.stream(values()).collect(Collectors.toMap(o -> o.symbol, o -> o));
    /**
     * Обязательные символы с которых начинается опция.
     */
    private static final String OPTION_PREFIX = "-";
    /**
     * Символ, который соответствует опции.
     */
    private final char symbol;
    /**
     * Флаг требования аргумента для опции.
     */
    private final boolean hasArgument;

    Option(char symbol, boolean hasArgument) {
        this.symbol = symbol;
        this.hasArgument = hasArgument;
    }

    /**
     * Проверяет что строка не может являться опцией.
     *
     * @param string проверяемая строка.
     * @return true если строка не может являться опцией.
     */
    public static boolean isNotOption(String string) {
        return !string.startsWith(OPTION_PREFIX);
    }

    /**
     * Производит поиск опции по заданной строке.
     * Возвращает {@link Optional контейнер} с соответствующей опцией или пустой если нет подходящей.
     *
     * @param string строка по которому проверяется соответствие.
     * @return Контейнер с соответствующей опцией либо пустой если не нашлось такой опции.
     */
    public static Optional<Option> findByString(String string) {
        if (isNotOption(string)) {
            return Optional.empty();
        }

        return Optional.ofNullable(OPTIONS.get(string.charAt(OPTION_PREFIX.length())));
    }

    /**
     * Парсит строку и производит декомпозицию слипшихся опций.
     * Если строка не может являться опцией или слипшимися опциями, тогда возвращается список из одной этой строки.
     * Если строка состоит из одной опции, тогда возвращается список из одной этой строки.
     * Если строка состоит из нескольких слипшихся опций и(или) из опции, слипшейся со своим аргументом,
     * тогда она разделяется на одиночные опции и аргумент крайней опции, если он присутствует, и возвращается
     * список опций в виде строк (без слипшихся опций), при этом изначальный порядок не нарушается.
     *
     * @param string проверяемая строка.
     * @return Список аргументов без слипшихся опций в виде отдельных строк.
     * @throws InvalidInputException если встречается неизвестная или пустая опция.
     */
    public static List<String> decompose(String string) {
        if (isNotOption(string)) {
            return List.of(string);
        }

        if (OPTION_PREFIX.equals(string)) {
            throw new InvalidInputException("пустая опция %s", OPTION_PREFIX);
        }

        List<String> arguments = new ArrayList<>();

        for (int i = OPTION_PREFIX.length(); i < string.length(); i++) {
            char symbol = string.charAt(i);
            Option option = OPTIONS.get(symbol);

            if (option == null) {
                throw new InvalidInputException("не известная опция %s%c", OPTION_PREFIX, symbol);
            }

            arguments.add(OPTION_PREFIX + symbol);

            if (option.hasArgument() && i + 1 < string.length()) {
                arguments.add(string.substring(i + 1));
                break;
            }
        }

        return arguments;
    }

    /**
     * Определяет для данной опции требуется аргумент или нет.
     *
     * @return true если требуется аргумент.
     */
    public boolean hasArgument() {
        return hasArgument;
    }

    @Override
    public String toString() {
        return hasArgument ?
                OPTION_PREFIX + symbol + " [аргумент]" :
                OPTION_PREFIX + symbol;
    }
}

package ru.cft.drozdetskiy.args;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Опции программы: {@linkplain #SET_DIRECTORY}, {@linkplain #SET_PREFIX}, {@linkplain #APPEND_FILES},
 * {@linkplain #SIMPLE_STAT}, {@linkplain #FULL_STAT}
 * Опция либо требует аргумент (option argument), либо нет.
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
     * Производит поиск {@linkplain Option опции} по заданному символу.
     * Возвращает {@link Optional контейнер} с соответствующей опцией или пустой если нет таковой.
     *
     * @param symbol символ по которому проверяется соответствие.
     * @return Контейнер с соответствующей опцией заданному символу либо пустой если не нашлось такой опции.
     */
    public static Optional<Option> findBySymbol(char symbol) {
        return Optional.ofNullable(OPTIONS.get(symbol));
    }

    /**
     * Определяет для данной опции требуется аргумент или нет.
     *
     * @return true если требуется аргумент.
     */
    public boolean hasArgument() {
        return hasArgument;
    }
}

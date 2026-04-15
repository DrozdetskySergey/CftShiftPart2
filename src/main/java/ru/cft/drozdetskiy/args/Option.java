package ru.cft.drozdetskiy.args;

import java.util.Optional;

/**
 * Опции программы (аргументы начинающиеся с символа минус):
 * {@linkplain #SET_DIRECTORY}, {@linkplain #SET_PREFIX}, {@linkplain #APPEND_FILES},
 * {@linkplain #SIMPLE_STAT}, {@linkplain #FULL_STAT}
 */
enum Option {

    /**
     * Задать каталог для файлов с результатом. Синтаксис: -o каталог
     */
    SET_DIRECTORY('o'),
    /**
     * Задать префикс для имён файлов с результатом. Синтаксис: -p префикс
     */
    SET_PREFIX('p'),
    /**
     * Включить режим записи в конец файла. Синтаксис: -a
     */
    APPEND_FILES('a'),
    /**
     * Собирать краткую статистику. Синтаксис: -s
     */
    SIMPLE_STAT('s'),
    /**
     * Собирать полную статистику. Синтаксис: -f
     */
    FULL_STAT('f');

    private final char symbol;

    Option(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Производит поиск {@linkplain Option опции} по заданному символу.
     * Возвращает {@link Optional контейнер} с соответствующей опцией или пустой если нет таковой.
     *
     * @param symbol символ по которому проверяется соответствие.
     * @return Контейнер с соответствующей опцией заданному символу либо пустой если не нашлось такой опции.
     */
    public static Optional<Option> findBySymbol(char symbol) {
        for (Option option : values()) {
            if (option.symbol == symbol) {

                return Optional.of(option);
            }
        }

        return Optional.empty();
    }

    /**
     * @return Символ соответствующий данной {@linkplain Option опции}.
     */
    public char getSymbol() {
        return symbol;
    }
}

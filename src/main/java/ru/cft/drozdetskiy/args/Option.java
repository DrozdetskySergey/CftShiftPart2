package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.InvalidInputException;

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
     * Если соответствие не обнаружилось, то бросает исключение.
     *
     * @param symbol символ по которому проверяется соответствие.
     * @return {@linkplain Option Опция} соответствующая заданному символу.
     * @throws InvalidInputException если заданный символ не соответствует какой-либо {@linkplain Option опции}.
     */
    public static Option fromSymbolOrThrow(char symbol) {
        for (Option o : values()) {
            if (o.symbol == symbol) {

                return o;
            }
        }

        throw new InvalidInputException("опция -'%c'", symbol);
    }

    /**
     * @return Символ соответствующий данной {@linkplain Option опции}.
     */
    public char getSymbol() {
        return symbol;
    }
}

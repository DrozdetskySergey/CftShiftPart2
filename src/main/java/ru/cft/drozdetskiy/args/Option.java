package ru.cft.drozdetskiy.args;

/**
 * Опции программы (аргументы начинающиеся с символа '-') переданные в main:
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

    final char symbol;

    Option(char symbol) {
        this.symbol = symbol;
    }
}

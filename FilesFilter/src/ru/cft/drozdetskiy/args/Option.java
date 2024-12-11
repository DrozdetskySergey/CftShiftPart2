package ru.cft.drozdetskiy.args;

enum Option {

    SET_FOLDER('o'),
    SET_PREFIX('p'),
    APPEND_FILES('a'),
    SIMPLE_STAT('s'),
    FULL_STAT('f');

    final char symbol;

    Option(char symbol) {
        this.symbol = symbol;
    }
}

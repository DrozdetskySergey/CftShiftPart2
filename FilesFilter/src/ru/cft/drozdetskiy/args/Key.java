package ru.cft.drozdetskiy.args;

enum Key {

    SET_FOLDER('o'),
    SET_PREFIX('p'),
    APPEND_FILES('a'),
    SIMPLE_STAT('s'),
    FULL_STAT('f');

    final char symbol;

    Key(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return symbol;
    }
}

package ru.cft.drozdetskiy.args;

enum Option {

    SET_FOLDER('o'),
    SET_PREFIX('p'),
    APPEND_FILES('a'),
    SIMPLE_STAT('s'),
    FULL_STAT('f');

    final char option;

    Option(char option) {
        this.option = option;
    }
}

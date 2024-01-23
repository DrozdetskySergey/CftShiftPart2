package ru.cft.drozdetskiy.args;

enum Key {

    SET_FOLDER('o'),
    SET_PREFIX('p'),
    APPEND_FILES('a'),
    SIMPLE_STAT('s'),
    FULL_STAT('f');

    final char key;

    Key(char key) {
        this.key = key;
    }
}

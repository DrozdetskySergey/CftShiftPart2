package ru.cft.drozdetskiy.args;

enum Key {

    SET_FOLDER("-o"),
    SET_PREFIX("-p"),
    APPEND_FILES("-a"),
    SIMPLE_STAT("-s"),
    FULL_STAT("-f");

    final String key;

    Key(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}

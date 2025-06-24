package ru.cft.drozdetskiy;

/**
 * Тип содержимого строки т.е. в какой объект можно распарсить всю строку:
 * {@linkplain #LONG}, {@linkplain #DOUBLE}, {@linkplain #STRING}
 */
enum ContentType {
    /**
     * Целое число.
     */
    LONG,
    /**
     * Вещественное число.
     */
    DOUBLE,
    /**
     * Строка.
     */
    STRING
}

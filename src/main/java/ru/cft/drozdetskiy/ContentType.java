package ru.cft.drozdetskiy;

/**
 * Можно распарсить строку в целое число, либо в вещественное число, либо оставить строкой.
 * Типы содержимого строки: {@linkplain #LONG}, {@linkplain #DOUBLE}, {@linkplain #STRING}
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

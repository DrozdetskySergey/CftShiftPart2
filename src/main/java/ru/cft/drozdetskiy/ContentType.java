package ru.cft.drozdetskiy;

/**
 * Можно распарсить строку в целое число, либо в вещественное число, либо оставить строкой.
 * Типы содержимого строки: {@linkplain #INTEGER}, {@linkplain #FLOAT}, {@linkplain #STRING}
 */
public enum ContentType {
    /**
     * Целое число.
     */
    INTEGER,
    /**
     * Вещественное число.
     */
    FLOAT,
    /**
     * Строка.
     */
    STRING
}

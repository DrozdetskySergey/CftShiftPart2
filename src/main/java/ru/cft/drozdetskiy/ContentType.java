package ru.cft.drozdetskiy;

/**
 * Типы содержимого строки: {@linkplain #INTEGER}, {@linkplain #FLOAT}, {@linkplain #STRING}
 * Можно распарсить строку в целое число или в вещественное число, либо оставить строкой.
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

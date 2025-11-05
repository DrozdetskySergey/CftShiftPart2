package ru.cft.drozdetskiy;

/**
 * Типы содержимого строки: {@linkplain #INTEGER}, {@linkplain #FLOAT}, {@linkplain #STRING}
 * то есть можно распарсить строку в целое число или в вещественное число, либо оставить строкой.
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

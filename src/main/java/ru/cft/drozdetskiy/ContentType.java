package ru.cft.drozdetskiy;

/**
 * Типы содержимого строки: {@linkplain #INTEGER}, {@linkplain #FLOAT}, {@linkplain #STRING},
 * то есть некую строку можно распарсить в целое число либо в вещественное число или это простая строка (любые символы).
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

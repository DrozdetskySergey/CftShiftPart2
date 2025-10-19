package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Статистика о значениях определенного {@linkplain  ContentType типа} переданных в виде строки.
 * Предоставляет метод {@linkplain #include(String) include}
 */
public interface Statistics {

    /**
     * Статистически обрабатывает переданное значение определенного {@linkplain  ContentType типа} переданное
     * внутри строки и добавляет нужную информацию в статистику.
     *
     * @param value значение в виде строки о котором нужно добавить информацию в статистику.
     */
    void include(String value);
}

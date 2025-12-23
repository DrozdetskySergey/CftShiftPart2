package ru.cft.drozdetskiy.statistics;

/**
 * Статистика о значениях переданных в формате строки. Предоставляет метод {@linkplain #include(String)}
 */
public interface Statistics {

    /**
     * Статистически обрабатывает значение переданное в формате строки. Добавляет нужную информацию в статистику.
     *
     * @param value значение в виде строки о котором нужно добавить информацию в статистику.
     */
    void include(String value);
}

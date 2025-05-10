package ru.cft.drozdetskiy.statistics;

/**
 * Статистика об однотипных объектах. Предоставляет метод {@linkplain #include(Object) include}
 *
 * @param <T> тип объектов о которых собирает информацию эта статистика.
 */
public interface Statistics<T> {

    /**
     * Статистически обрабатывает переданный объект и добавляет нужную информацию в статистику.
     *
     * @param value объект о котором нужно добавить информацию в статистику.
     */
    void include(T value);
}

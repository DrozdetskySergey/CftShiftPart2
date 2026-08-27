package ru.cft.drozdetskiy.statistics;

import ru.cft.drozdetskiy.ContentType;

/**
 * Статистика о значениях переданных в формате строки. {@linkplain ContentType Тип содержимого} строк фиксируется
 * при создании этого объекта. Предоставляет методы: {@linkplain #include(String)}, {@linkplain #getContentType()},
 * {@linkplain #getCount()}, {@linkplain #getAdditionalInfo()}.
 */
public abstract class Statistics {

    /**
     * Тип содержимого строки, которая ожидается в качестве параметра в методе {@linkplain #include(String)}.
     */
    private final ContentType contentType;

    /**
     * Количество значений которые были переданы в данную статистику через метод {@linkplain #include(String)}
     * и удачно обработаны.
     */
    protected long count;

    /**
     * Статистика о значениях переданных в формате строки. {@linkplain ContentType Тип содержимого} строк фиксируется
     * при создании этого объекта.
     */
    public Statistics(ContentType contentType) {
        this.contentType = contentType;
    }

    /**
     * Отдаёт {@linkplain ContentType тип содержимого} строки, которая ожидается в качестве параметра у метода
     * {@linkplain #include(String)}. Он определяется при создании этого объекта статистики и никогда не меняется.
     *
     * @return тип содержимого строки.
     */
    public ContentType getContentType() {
        return contentType;
    }

    /**
     * Отдаёт количество значений которые были переданы в данную статистику через метод {@linkplain #include(String)}
     * и удачно обработаны.
     *
     * @return количество статистически обработанных значений.
     */
    public long getCount() {
        return count;
    }

    /**
     * Статистически обрабатывает значение переданное в формате строки и добавляет нужную информацию в данную статистику.
     * Ожидаемый тип значения (содержимое переданной строки) отдаёт метод {@linkplain #getContentType()}
     *
     * @param value значение в формате строки о котором нужно добавить информацию в статистику.
     * @throws NumberFormatException если статистика обрабатывает числовые значения, и переданное значение невалидное.
     */
    public abstract void include(String value);

    /**
     * Отдаёт строку в которой содержится вся дополнительная информация из данной статистики в удобочитаемом виде.
     *
     * @return дополнительная информация в виде строки.
     */
    public abstract String getAdditionalInfo();
}

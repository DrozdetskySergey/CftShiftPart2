package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.statistics.StatisticsFactory;

import java.nio.file.Path;
import java.util.List;

/**
 * Контейнер для передачи данных (DTO) пришедших в аргументы программы.
 *
 * @param prefix            Префикс имен файлов с результатом.
 * @param directory         Каталог для результата.
 * @param statisticsFactory Фабрика для создания объектов статистики.
 * @param isAppend          Флаг режима записи в конец файла.
 * @param files             Список путей к файлам.
 */
public record ArgumentsDTO(String prefix,
                           Path directory,
                           StatisticsFactory statisticsFactory,
                           boolean isAppend,
                           List<Path> files) {
}

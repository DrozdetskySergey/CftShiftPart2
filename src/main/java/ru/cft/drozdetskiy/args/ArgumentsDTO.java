package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.nio.file.Path;
import java.util.List;

/**
 * Контейнер для передачи данных (DTO) пришедших в аргументы программы. Создаётся через Builder.
 *
 * @param prefix         Префикс имен файлов с результатом.
 * @param directory      Каталог для результата.
 * @param statisticsType Тип требуемой статистики.
 * @param isAppend       Режим записи в конец файла.
 * @param files          Список путей к файлам.
 */
public record ArgumentsDTO(String prefix, Path directory, StatisticsType statisticsType, boolean isAppend,
                           List<Path> files) {

    /**
     * Builder. Встроенный статичный класс для сборки объекта класса {@link ArgumentsDTO}
     */
    public static final class Builder {

        private String prefix;
        private Path directory;
        private StatisticsType statisticsType;
        private boolean isAppend;
        private List<Path> files;

        /**
         * Builder. Задаёт префикс для имён файлов с результатом.
         *
         * @param prefix строка с префиксом.
         */
        public Builder prefix(String prefix) {
            this.prefix = prefix;

            return this;
        }

        /**
         * Builder. Задаёт каталог для файлов с результатом.
         *
         * @param directory каталог.
         */
        public Builder directory(Path directory) {
            this.directory = directory;

            return this;
        }

        /**
         * Builder. Задаёт тип требуемой статистики.
         *
         * @param statisticsType {@linkplain StatisticsType Тип статистики}.
         */
        public Builder statisticsType(StatisticsType statisticsType) {
            this.statisticsType = statisticsType;

            return this;
        }

        /**
         * Builder. Задаёт boolean значение режима записи в конец файла.
         *
         * @param isAppend true если режим записи в конец файла.
         */
        public Builder isAppend(boolean isAppend) {
            this.isAppend = isAppend;

            return this;
        }

        /**
         * Builder. Задаёт список путей к файлам.
         *
         * @param files список объектов интерфейса {@link Path}
         */
        public Builder files(List<Path> files) {
            this.files = files;

            return this;
        }

        /**
         * Builder. Завершает конструирование объекта.
         *
         * @return новый объект класса {@link ArgumentsDTO}
         */
        public ArgumentsDTO build() {
            return new ArgumentsDTO(prefix, directory, statisticsType, isAppend, files);
        }
    }
}

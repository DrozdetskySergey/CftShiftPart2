package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.nio.file.Path;
import java.util.List;

/**
 * Неизменяемый объект для передачи данных (DTO) пришедших в аргументы программы. Создаётся через Builder.
 */
public final class ArgumentsDTO {

    /**
     * Префикс имен файлов с результатом.
     */
    private final String prefix;
    /**
     * Каталог для результата.
     */
    private final String directory;
    /**
     * Тип требуемой статистики.
     */
    private final StatisticsType statisticsType;
    /**
     * Режим записи в конец файла.
     */
    private final boolean isAppend;
    /**
     * Список входящих файлов.
     */
    private final List<Path> files;

    /**
     * Неизменяемый объект для передачи данных (DTO) пришедших в аргументы программы. Создаётся через Builder.
     */
    private ArgumentsDTO(Builder builder) {
        prefix = builder.prefix == null ? "" : builder.prefix;
        directory = builder.directory == null ? "" : builder.directory;
        statisticsType = builder.statisticsType == null ? StatisticsType.SIMPLE : builder.statisticsType;
        isAppend = builder.isAppend;
        files = builder.files == null ? List.of() : List.copyOf(builder.files);
    }

    /**
     * Геттер. Отдаёт префикс для имён файлов с результатом.
     *
     * @return {@link String}
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Геттер. Отдаёт каталог для файлов с результатом.
     *
     * @return {@link String}
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Геттер. Отдаёт список файлов для чтения строк (входящие данные).
     *
     * @return неизменяемый список объектов класса {@link Path}
     */
    public List<Path> getFiles() {
        return files;
    }

    /**
     * Геттер. Отдаёт тип требуемой статистики.
     *
     * @return {@link StatisticsType}
     */
    public StatisticsType getStatisticsType() {
        return statisticsType;
    }

    /**
     * Геттер. Отдаёт boolean значение режима записи в конец файла.
     *
     * @return true если режим записи в конец файла.
     */
    public boolean isAppend() {
        return isAppend;
    }

    /**
     * Встроенный статичный класс для сборки объекта класса {@link ArgumentsDTO}
     */
    public static class Builder {

        private String prefix;
        private String directory;
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
         * @param directory строка с каталогом.
         */
        public Builder directory(String directory) {
            this.directory = directory;

            return this;
        }

        /**
         * Builder. Задаёт тип требуемой статистики.
         *
         * @param statisticsType {@link StatisticsType}
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
         * Builder. Задаёт список файлов для чтения строк (входящие данные).
         *
         * @param files список объектов класса {@link Path}
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
            return new ArgumentsDTO(this);
        }
    }
}

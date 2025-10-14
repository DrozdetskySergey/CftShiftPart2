package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.nio.file.Path;
import java.util.List;

/**
 * Контейнер для передачи данных (DTO) пришедших в аргументы программы. Создаётся через Builder.
 */
public final class ArgumentsDTO {

    /**
     * Префикс имен файлов с результатом.
     */
    private final String prefix;
    /**
     * Каталог для результата.
     */
    private final Path directory;
    /**
     * Тип требуемой статистики.
     */
    private final StatisticsType statisticsType;
    /**
     * Режим записи в конец файла.
     */
    private final boolean isAppend;
    /**
     * Список путей к файлам.
     */
    private final List<Path> files;

    /**
     * Контейнер для передачи данных (DTO) пришедших в аргументы программы. Создаётся через Builder.
     */
    private ArgumentsDTO(Builder builder) {
        prefix = builder.prefix;
        directory = builder.directory;
        statisticsType = builder.statisticsType;
        isAppend = builder.isAppend;
        files = builder.files;
    }

    /**
     * Геттер. Отдаёт префикс для имён файлов с результатом.
     *
     * @return Строка с префиксом.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Геттер. Отдаёт каталог для файлов с результатом.
     *
     * @return Каталог для результата.
     */
    public Path getDirectory() {
        return directory;
    }

    /**
     * Геттер. Отдаёт список путей к файлам.
     *
     * @return Список объектов интерфейса {@link Path}
     */
    public List<Path> getFiles() {
        return files;
    }

    /**
     * Геттер. Отдаёт тип требуемой статистики.
     *
     * @return {@linkplain StatisticsType Тип статистики}.
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
            return new ArgumentsDTO(this);
        }
    }
}

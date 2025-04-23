package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.util.List;

/**
 * Объект для передачи данных пришедших через аргументы.
 */
public final class ArgumentsDTO {

    /**
     * Префикс имен файлов с результатом.
     */
    private final String prefix;
    /**
     * Папка для результата.
     */
    private final String folder;
    /**
     * Тип собираемой статистики.
     */
    private final StatisticsType statisticsType;
    /**
     * Режим записи в конец файла если он уже существует.
     */
    private final boolean isAppend;
    /**
     * Список имён входящих файлов.
     */
    private final List<String> files;

    private ArgumentsDTO(Builder builder) {
        prefix = builder.prefix == null ? "" : builder.prefix;
        folder = builder.folder == null ? "" : builder.folder;
        statisticsType = builder.statisticsType == null ? StatisticsType.SIMPLE : builder.statisticsType;
        isAppend = builder.isAppend;
        files = builder.files == null ? List.of() : List.copyOf(builder.files);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getFolder() {
        return folder;
    }

    public List<String> getFiles() {
        return files;
    }

    public StatisticsType getStatisticsType() {
        return statisticsType;
    }

    public boolean isAppend() {
        return isAppend;
    }

    /**
     * Встроенный статичный класс для сборки объекта класса {@link ArgumentsDTO}
     */
    public static class Builder {

        private String prefix;
        private String folder;
        private StatisticsType statisticsType;
        private boolean isAppend;
        private List<String> files;

        public Builder prefix(String prefix) {
            this.prefix = prefix;

            return this;
        }

        public Builder folder(String folder) {
            this.folder = folder;

            return this;
        }

        public Builder statisticsType(StatisticsType statisticsType) {
            this.statisticsType = statisticsType;

            return this;
        }

        public Builder isAppend(boolean isAppend) {
            this.isAppend = isAppend;

            return this;
        }

        public Builder files(List<String> files) {
            this.files = files;

            return this;
        }

        public ArgumentsDTO build() {
            return new ArgumentsDTO(this);
        }
    }
}

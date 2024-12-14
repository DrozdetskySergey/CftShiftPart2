package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.util.List;

public final class ArgumentsDTO {

    private final String prefix;
    private final String folder;
    private final String unknownOptions;
    private final StatisticsType statisticsType;
    private final boolean isAppend;
    private final List<String> files;

    private ArgumentsDTO(Builder builder) {
        this.prefix = builder.prefix == null ? "" : builder.prefix;
        this.folder = builder.folder == null ? "" : builder.folder;
        this.unknownOptions = builder.unknownOptions == null ? "" : builder.unknownOptions;
        this.statisticsType = builder.statisticsType == null ? StatisticsType.SIMPLE : builder.statisticsType;
        this.isAppend = builder.isAppend;
        this.files = builder.files == null ? List.of() : List.copyOf(builder.files);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getFolder() {
        return folder;
    }

    public String getUnknownOptions() {
        return unknownOptions;
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

    public static class Builder {

        private String prefix;
        private String folder;
        private String unknownOptions;
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

        public Builder unknownOptions(String unknownOptions) {
            this.unknownOptions = unknownOptions;

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

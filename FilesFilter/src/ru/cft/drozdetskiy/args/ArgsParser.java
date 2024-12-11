package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.cft.drozdetskiy.args.Option.*;

public class ArgsParser {

    private final List<String> files = new ArrayList<>();
    private StatisticsType statisticsType = StatisticsType.SIMPLE;
    private boolean isAppend = false;
    private final StringBuilder prefix = new StringBuilder();
    private final StringBuilder folder = new StringBuilder();
    private final StringBuilder unknownOptions = new StringBuilder();

    public ArgsParser(String[] args) {
        List<String> arguments = decompose(filter(args));

        for (Iterator<String> iterator = arguments.iterator(); iterator.hasNext(); ) {
            String argument = iterator.next();

            if (isNotOption(argument)) {
                files.add(argument);
            } else {
                char option = argument.charAt(1);

                if (option == APPEND_FILES.symbol) {
                    isAppend = true;
                } else if (option == SIMPLE_STAT.symbol) {
                    statisticsType = StatisticsType.SIMPLE;
                } else if (option == FULL_STAT.symbol) {
                    statisticsType = StatisticsType.FULL;
                } else if (option == SET_FOLDER.symbol && iterator.hasNext()) {
                    folder.append(iterator.next());
                } else if (option == SET_PREFIX.symbol && iterator.hasNext()) {
                    prefix.append(iterator.next());
                } else {
                    unknownOptions.append(option);
                }
            }
        }
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

    public String getPrefix() {
        return prefix.toString();
    }

    public String getFolder() {
        return folder.toString();
    }

    public String getUnknownOptions() {
        return unknownOptions.toString();
    }

    private boolean isNotOption(String string) {
        return !string.startsWith("-");
    }

    private List<String> filter(String[] args) {
        return Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(String::strip)
                .collect(Collectors.toList());
    }

    private List<String> decompose(List<String> arguments) {
        List<String> result = new ArrayList<>();

        for (String s : arguments) {
            if (isNotOption(s)) {
                result.add(s);
            } else {
                for (int i = 1; i < s.length(); i++) {
                    char symbol = s.charAt(i);
                    result.add("-" + symbol);

                    if ((symbol == SET_FOLDER.symbol || symbol == SET_PREFIX.symbol) && (i + 1 < s.length())) {
                        result.add(s.substring(i + 1));
                        break;
                    }
                }
            }
        }

        return result;
    }
}

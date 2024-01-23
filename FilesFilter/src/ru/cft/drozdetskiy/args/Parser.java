package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.cft.drozdetskiy.args.Key.*;

public class Parser {

    private final List<String> inputFiles = new ArrayList<>();
    private StatisticsType statisticsType = StatisticsType.SIMPLE;
    private boolean isAppend = false;
    private String prefix = "";
    private String folder = "";

    public Parser(String[] args) {
        List<String> arguments = decompose(filter(args));

        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i).startsWith("-")) {
                char key = arguments.get(i).charAt(1);

                if (key == APPEND_FILES.symbol) {
                    isAppend = true;
                } else if (key == SIMPLE_STAT.symbol) {
                    statisticsType = StatisticsType.SIMPLE;
                } else if (key == FULL_STAT.symbol) {
                    statisticsType = StatisticsType.FULL;
                } else if (key == SET_FOLDER.symbol && i < arguments.size() - 1 && !arguments.get(i + 1).startsWith("-")) {
                    folder = arguments.get(++i);
                } else if (key == SET_PREFIX.symbol && i < arguments.size() - 1 && !arguments.get(i + 1).startsWith("-")) {
                    prefix = arguments.get(++i);
                } else {
                    System.out.printf("Не известный параметр: %c%n", key);
                }
            } else {
                inputFiles.add(arguments.get(i));
            }
        }
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }

    public StatisticsType getStatisticsType() {
        return statisticsType;
    }

    public boolean isAppend() {
        return isAppend;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getFolder() {
        return folder;
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
            if (s.startsWith("-")) {
                for (int i = 1; i < s.length(); i++) {
                    char key = s.charAt(i);
                    result.add("-" + key);

                    if ((key == SET_FOLDER.symbol || key == SET_PREFIX.symbol) && (i < s.length() - 1)) {
                        result.add(s.substring(i + 1));
                        break;
                    }
                }
            } else {
                result.add(s);
            }
        }

        return result;
    }
}

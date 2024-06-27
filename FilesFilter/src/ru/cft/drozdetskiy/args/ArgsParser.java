package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.cft.drozdetskiy.args.Key.*;

public class ArgsParser {

    private final List<String> inputFiles = new ArrayList<>();
    private StatisticsType statisticsType = StatisticsType.SIMPLE;
    private boolean isAppend = false;
    private final StringBuilder prefix = new StringBuilder();
    private final StringBuilder folder = new StringBuilder();
    private final StringBuilder unknownKeys = new StringBuilder();

    public ArgsParser(String[] args) {
        List<String> arguments = decompose(filter(args));

        for (Iterator<String> iterator = arguments.iterator(); iterator.hasNext(); ) {
            String argument = iterator.next();

            if (isNotKey(argument)) {
                inputFiles.add(argument);
            } else {
                char key = argument.charAt(1);

                if (key == APPEND_FILES.key) {
                    isAppend = true;
                } else if (key == SIMPLE_STAT.key) {
                    statisticsType = StatisticsType.SIMPLE;
                } else if (key == FULL_STAT.key) {
                    statisticsType = StatisticsType.FULL;
                } else if (key == SET_FOLDER.key && iterator.hasNext()) {
                    argument = iterator.next();
                    folder.append(argument);
                } else if (key == SET_PREFIX.key && iterator.hasNext()) {
                    argument = iterator.next();
                    prefix.append(argument);
                } else {
                    unknownKeys.append(key);
                }
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
        return prefix.toString();
    }

    public String getFolder() {
        return folder.toString();
    }

    public String getUnknownKeys() {
        return unknownKeys.toString();
    }

    private boolean isNotKey(String string) {
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
            if (isNotKey(s)) {
                result.add(s);
            } else {
                for (int i = 1; i < s.length(); i++) {
                    char key = s.charAt(i);
                    result.add("-" + key);

                    if ((key == SET_FOLDER.key || key == SET_PREFIX.key) && (i + 1 < s.length())) {
                        result.add(s.substring(i + 1));
                        break;
                    }
                }
            }
        }

        return result;
    }
}

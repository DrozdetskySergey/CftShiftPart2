package ru.cft.drozdetskiy.args;

import ru.cft.drozdetskiy.statistics.StatisticsType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.cft.drozdetskiy.args.Option.*;

public final class ArgumentsParser {

    private ArgumentsParser() {
    }

    public static ArgumentsDTO parse(String[] args) {
        StringBuilder prefix = new StringBuilder();
        StringBuilder folder = new StringBuilder();
        StringBuilder wrongArguments = new StringBuilder();
        StatisticsType statisticsType = StatisticsType.SIMPLE;
        boolean isAppend = false;
        List<String> files = new ArrayList<>();
        List<String> arguments = decompose(filter(args));

        for (Iterator<String> iterator = arguments.iterator(); iterator.hasNext(); ) {
            String argument = iterator.next();

            if (isNotOption(argument)) {
                files.add(argument);
            } else {
                char symbol = argument.charAt(1);

                if (symbol == APPEND_FILES.symbol) {
                    isAppend = true;
                } else if (symbol == SIMPLE_STAT.symbol) {
                    statisticsType = StatisticsType.SIMPLE;
                } else if (symbol == FULL_STAT.symbol) {
                    statisticsType = StatisticsType.FULL;
                } else if (symbol == SET_FOLDER.symbol && iterator.hasNext()) {
                    folder.append(iterator.next());
                } else if (symbol == SET_PREFIX.symbol && iterator.hasNext()) {
                    prefix.append(iterator.next());
                } else {
                    wrongArguments.append(symbol);
                }
            }
        }

        return new ArgumentsDTO.Builder()
                .prefix(prefix.toString())
                .folder(folder.toString())
                .wrongArguments(wrongArguments.toString())
                .statisticsType(statisticsType)
                .isAppend(isAppend)
                .files(files)
                .build();
    }

    private static boolean isNotOption(String string) {
        return !string.startsWith("-");
    }

    private static List<String> filter(String[] args) {
        return Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(String::strip)
                .collect(Collectors.toList());
    }

    private static List<String> decompose(List<String> arguments) {
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

package ru.cft.drozdetskiy.statistics;

/**
 * Функциональный класс. Специализируется на печати объекта {@linkplain Statistics} в {@linkplain System#out}.
 * Реализует статичный метод {@linkplain #println(Statistics)}
 */
public final class StatisticsPrinter {

    private StatisticsPrinter() {
    }

    /**
     * Собирает и отдаёт строку которая, содержит всю информацию из переданного объекта {@linkplain Statistics}.
     *
     * @param statistics объект {@linkplain Statistics}.
     * @return Строка.
     */
    private static String buildString(Statistics statistics) {
        String content = switch (statistics.getContentType()) {
            case INTEGER -> "целых чисел";
            case FLOAT -> "вещественных чисел";
            case STRING -> "строк";
        };

        return String.format("Количество %s = %d%n%s", content, statistics.getCount(), statistics.getAdditionalInfo());
    }

    /**
     * Печатает объект {@linkplain Statistics} в {@linkplain System#out}.
     *
     * @param statistics объект {@linkplain Statistics}.
     */
    public static void println(Statistics statistics) {
        System.out.println(buildString(statistics));
    }
}

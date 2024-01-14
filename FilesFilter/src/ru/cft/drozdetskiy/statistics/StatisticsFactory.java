package ru.cft.drozdetskiy.statistics;

public interface StatisticsFactory {

    Statistics<Long> createForLong();

    Statistics<Double> createForDouble();

    Statistics<String> createForString();
}

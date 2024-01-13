package ru.cft.drozdetskiy.statistics;

public interface StatisticsFactory {

    Statistics<Long> createForInteger();

    Statistics<Double> createForDouble();

    Statistics<String> createForString();
}

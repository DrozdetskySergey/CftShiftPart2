package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.buffer.impl.FastBuffer;
import ru.cft.drozdetskiy.statistics.StatisticsException;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsFactoryBuilder;
import ru.cft.drozdetskiy.statistics.StatisticsType;
import ru.cft.drozdetskiy.supplier.StringSupplier;
import ru.cft.drozdetskiy.supplier.impl.FileStringSupplier;
import ru.cft.drozdetskiy.writer.FileWriterFromBuffer;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> inputFiles = Arrays.asList("in1", "in2", "in3");
        StatisticsType statisticsType = StatisticsType.FULL;
        boolean isAppend = false;
        String folderPlusPrefix = "";

        try (StringSupplier supplier = new FileStringSupplier(inputFiles)) {
            StatisticsFactory factory = StatisticsFactoryBuilder.buildFactory(statisticsType);
            Buffer<Long> longBuffer = new FastBuffer<>(factory.createForLong());
            Buffer<Double> doubleBuffer = new FastBuffer<>(factory.createForDouble());
            Buffer<String> stringBuffer = new FastBuffer<>(factory.createForString());

            Filter.divide(supplier, longBuffer, doubleBuffer, stringBuffer);

            System.out.println(longBuffer.getStatisticsInfo());
            System.out.println(doubleBuffer.getStatisticsInfo());
            System.out.println(stringBuffer.getStatisticsInfo());

            if (longBuffer.size() > 0) {
                String longFile = folderPlusPrefix + "integers.txt";
                FileWriterFromBuffer<Long> longWriter = new FileWriterFromBuffer<>(longFile, longBuffer, isAppend);
                longWriter.run();
            }

            if (doubleBuffer.size() > 0) {
                String doubleFile = folderPlusPrefix + "floats.txt";
                FileWriterFromBuffer<Double> doubleWriter = new FileWriterFromBuffer<>(doubleFile, doubleBuffer, isAppend);
                doubleWriter.run();
            }

            if (stringBuffer.size() > 0) {
                String stringFile = folderPlusPrefix + "strings.txt";
                FileWriterFromBuffer<String> stringWriter = new FileWriterFromBuffer<>(stringFile, stringBuffer, isAppend);
                stringWriter.run();
            }
        } catch (StatisticsException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

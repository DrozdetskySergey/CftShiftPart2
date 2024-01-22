package ru.cft.drozdetskiy;

import ru.cft.drozdetskiy.buffer.Buffer;
import ru.cft.drozdetskiy.buffer.impl.FastBuffer;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;
import ru.cft.drozdetskiy.statistics.StatisticsFactoryBuilder;
import ru.cft.drozdetskiy.statistics.StatisticsType;
import ru.cft.drozdetskiy.supplier.StringSupplier;
import ru.cft.drozdetskiy.supplier.impl.FileStringSupplier;
import ru.cft.drozdetskiy.writer.FileWriterFromBuffer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> inputFiles = Arrays.asList("in1", "in2", "in3");
        StatisticsType statisticsType = StatisticsType.FULL;
        boolean isAppend = true;
        String prefix = "";
        String folder = "";

        try (StringSupplier supplier = new FileStringSupplier(inputFiles)) {
            StatisticsFactory statisticsFactory = StatisticsFactoryBuilder.buildFactory(statisticsType);
            Buffer<Long> longBuffer = new FastBuffer<>(statisticsFactory.createForLong());
            Buffer<Double> doubleBuffer = new FastBuffer<>(statisticsFactory.createForDouble());
            Buffer<String> stringBuffer = new FastBuffer<>(statisticsFactory.createForString());

            Filter.divide(supplier, longBuffer, doubleBuffer, stringBuffer);

            System.out.println(longBuffer.getStatisticsInfo());
            System.out.println(doubleBuffer.getStatisticsInfo());
            System.out.println(stringBuffer.getStatisticsInfo());

            folder += "\\";

            if (!Paths.get(folder).isAbsolute()) {
                folder = Paths.get("").toAbsolutePath().toString() + "\\" + folder;
            }

            Files.createDirectories(Paths.get(folder));

            if (longBuffer.size() > 0) {
                Path longFile = Paths.get(folder + prefix + "integers.txt");
                FileWriterFromBuffer<Long> longWriter = new FileWriterFromBuffer<>(longFile, longBuffer, isAppend);
                longWriter.run();
            }

            if (doubleBuffer.size() > 0) {
                Path doubleFile = Paths.get(folder + prefix + "floats.txt");
                FileWriterFromBuffer<Double> doubleWriter = new FileWriterFromBuffer<>(doubleFile, doubleBuffer, isAppend);
                doubleWriter.run();
            }

            if (stringBuffer.size() > 0) {
                Path stringFile = Paths.get(folder + prefix + "strings.txt");
                FileWriterFromBuffer<String> stringWriter = new FileWriterFromBuffer<>(stringFile, stringBuffer, isAppend);
                stringWriter.run();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

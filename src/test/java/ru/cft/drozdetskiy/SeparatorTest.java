package ru.cft.drozdetskiy;

import org.junit.jupiter.api.Test;
import ru.cft.drozdetskiy.statistics.StatisticsFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.cft.drozdetskiy.ContentType.*;

class SeparatorTest {

    @Test
    void handleStringsWithIntegerContentType() throws IOException {
        List<String> input = List.of(
                "0", "+1", "-0", "+000000001234567890987654321234567890", "-1000000000000000000000000000000000",
                "000000000000000000000000000000000000000000000000000000000000064575675686787685868678567834219139033",
                "-109834579857960667496011047664600000000000000000000000000000000000000000000000000000000000000000000"
        );
        StringBuilder integers = new StringBuilder();
        Separator separator = new Separator(Map.of(INTEGER, integers, FLOAT, new StringBuilder(), STRING, new StringBuilder()));

        separator.handleStrings(input.iterator(), StatisticsFactory.FULL);
        List<String> result = Arrays.stream(integers.toString().split(System.lineSeparator())).toList();

        assertEquals(input, result);
    }

    @Test
    void handleStringsWithFloatContentType() throws IOException {
        List<String> input = List.of(
                "1.0", "+0.0", "3.1415", "+314.15E-2", "-.45", "-15.e+25", ".0", "0.", "+0.", "-.0", ".0e0",
                "+0E+0", "1.E-1", "0.e-224", "0.E0", "470E+12", "03e0", "1234567890.0987654321E2147483647",
                "12398124683687003003004355001000000000000000000000000000000000000000000000000000000E-2147483647",
                "000000000987654321e00000000000000000000000000000000000000000000000000000000000000000000009999",
                "17529357297467465715594198536456918734616687648431768347563875634875187345.1764987236489174681274612346283476287461283476875685764385761874659183768347519876518768768174583745613856348563285718973264461464165764517451345613"
        );
        StringBuilder floats = new StringBuilder();
        Separator separator = new Separator(Map.of(INTEGER, new StringBuilder(), FLOAT, floats, STRING, new StringBuilder()));

        System.out.println(separator.handleStrings(input.iterator(), StatisticsFactory.FULL));
        List<String> result = Arrays.stream(floats.toString().split(System.lineSeparator())).toList();

        assertEquals(input, result);
    }

    @Test
    void handleStringsWithStringContentType() throws IOException {
        List<String> input = List.of(
                "", " ", "Java", ".", "E1", "-.e5", "042.1415.3", "+27eE7", "1567e+", "99E2.5", "+.", "-", "..",
                "e5", "3E3E3", "6.e000-", ".e0", "777E ", "80.08e", "22e-2147483648", "+4.05E12345678901", "+-1",
                "0-8", ".+3", "+.E357", "-412E61.0", "22.E+", "22.E+50E22"
        );
        StringBuilder strings = new StringBuilder();
        Separator separator = new Separator(Map.of(INTEGER, new StringBuilder(), FLOAT, new StringBuilder(), STRING, strings));

        separator.handleStrings(input.iterator(), StatisticsFactory.FULL);
        List<String> result = Arrays.stream(strings.toString().split(System.lineSeparator())).toList();

        assertEquals(input, result);
    }
}
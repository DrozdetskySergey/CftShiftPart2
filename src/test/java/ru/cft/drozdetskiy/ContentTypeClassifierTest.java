package ru.cft.drozdetskiy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.cft.drozdetskiy.ContentType.*;

class ContentTypeClassifierTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "0", "+1", "-0",
            "+000000001234567890987654321234567890",
            "-1000000000000000000000000000000000",
            "000000000000000000000000000000000000000000000000000000000000064575675686787685868678567834219139033",
            "-109834579857960667496011047664600000000000000000000000000000000000000000000000000000000000000000000"
    })
    void classifyAsInteger(String input) {
        assertEquals(INTEGER, ContentTypeClassifier.classify(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1.0", "+0.0", "3.1415", "+314.15E-2", "-.45", "-15.e+25", ".0", "0.", "+0.", "-.0", ".0e0",
            "+0E+0", "1.E-1", "0.e-224", "0.E0", "470E+12", "03e0",
            "1234567890.0987654321E2147483647",
            "12398124683687003003004355001000000000000000000000000000000000000000000000000000000E-2147483647",
            "000000000987654321e00000000000000000000000000000000000000000000000000000000000000000000009999"
    })
    void classifyAsFloat(String input) {
        assertEquals(FLOAT, ContentTypeClassifier.classify(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", " ", "Java", ".", "E1", "-.e5", "042.1415.3", "+27eE7", "1567e+", "99E2.5", "+.", "-", "..",
            "e5", "3E3E3", "6.e000-", ".e0", "777E ", "80.08e", "22e-2147483648", "+4.05E12345678901", "+-1",
            "0-8", ".+3", "+.E357", "-412E61.0", "22.E+", "22.E+50E22"
    })
    void classifyAsString(String input) {
        assertEquals(STRING, ContentTypeClassifier.classify(input));
    }
}
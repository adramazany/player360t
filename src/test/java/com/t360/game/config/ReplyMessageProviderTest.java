package com.t360.game.config;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


class ReplyMessageProviderTest {

    @ParameterizedTest
    @CsvSource(value = {"message 0:message 0 1","message 0 1:message 0 1 1","message 0 1 1:message 0 1 1 2"}, delimiter = ':')
    void parseAndReply(String input, String expected) {
        String actual = ReplyMessageProvider.parseAndReply(input);
        assertEquals(expected, actual);
    }
}
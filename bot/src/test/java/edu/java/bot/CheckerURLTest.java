package edu.java.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class CheckerURLTest {
    @ParameterizedTest
    @ValueSource(strings = {"https://github.com/dolmitya/tinkoff_curs_tgBot/pulls"
        , "https://github.com/Anoldyouth/Java-Telegram-Notifications/pull/1/files#diff-4de"})
    @DisplayName("check valid URL")
    void testValidURL(String url) {
        assertTrue(CheckerURL.check(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://git.com/dolmitya/tinkoff_curs_tgBot/pulls"
        , "htt"})
    @DisplayName("check valid URL")
    void testInvalidURL(String url) {
        assertFalse(CheckerURL.check(url));
    }
}

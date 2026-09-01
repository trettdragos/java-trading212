package io.github.trettdragos.trading212;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Trading212ClientTest {

    @AfterEach
    void clearProperty() {
        System.clearProperty(Trading212Client.API_KEY_PROPERTY);
    }

    @Test
    void build_fallsBackToApiKeySystemPropertyWhenBuilderApiKeyIsNotSet() {
        System.setProperty(Trading212Client.API_KEY_PROPERTY, "prop-key");

        assertDoesNotThrow(() -> Trading212Client.builder()
                .environment(Trading212Environment.DEMO)
                .build());
    }

    @Test
    void build_explicitApiKeyTakesPrecedenceOverSystemProperty() {
        System.setProperty(Trading212Client.API_KEY_PROPERTY, "prop-key");

        assertDoesNotThrow(() -> Trading212Client.builder()
                .environment(Trading212Environment.DEMO)
                .apiKey("explicit-key")
                .build());
    }

    @Test
    void build_throwsWhenNeitherBuilderApiKeyNorSystemPropertyIsSet() {
        assertThrows(IllegalStateException.class, () -> Trading212Client.builder()
                .environment(Trading212Environment.DEMO)
                .build());
    }
}

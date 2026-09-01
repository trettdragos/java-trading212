package io.github.trettdragos.trading212.http;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.trettdragos.trading212.Trading212ApiException;
import io.github.trettdragos.trading212.model.account.AccountInfo;
import io.github.trettdragos.trading212.testutil.StubServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpTransportTest {

    private StubServer server;

    @BeforeEach
    void setUp() {
        server = new StubServer();
    }

    @AfterEach
    void tearDown() {
        server.close();
    }

    @Test
    void get_retriesOnRateLimitAndHonorsRetryAfterHeader() {
        server.enqueue(429, "rate limited", Map.of("Retry-After", "0"));
        server.enqueue(200, """
                {"currencyCode": "EUR", "id": 7}
                """);

        HttpTransport transport = new HttpTransport(server.baseUrl(), "key", HttpClient.newHttpClient(),
                new RetryPolicy(3, Duration.ofMillis(1), Duration.ofMillis(50)));

        AccountInfo info = transport.get("/api/v0/equity/account/info", new TypeReference<>() {});

        assertEquals("EUR", info.currencyCode());
        assertEquals(2, server.requests().size());
    }

    @Test
    void get_givesUpAfterMaxRetriesAndThrowsApiException() {
        server.enqueue(429, "rate limited", Map.of());
        server.enqueue(429, "rate limited", Map.of());

        HttpTransport transport = new HttpTransport(server.baseUrl(), "key", HttpClient.newHttpClient(),
                new RetryPolicy(1, Duration.ofMillis(1), Duration.ofMillis(10)));

        Trading212ApiException exception = assertThrows(Trading212ApiException.class,
                () -> transport.get("/api/v0/equity/account/info", new TypeReference<AccountInfo>() {}));

        assertEquals(429, exception.statusCode());
        assertTrue(exception.isRateLimited());
        assertEquals(2, server.requests().size());
    }

    @Test
    void get_nonRateLimitErrorFailsImmediatelyWithoutRetrying() {
        server.enqueue(403, "forbidden", Map.of());

        HttpTransport transport = new HttpTransport(server.baseUrl(), "key", HttpClient.newHttpClient(), RetryPolicy.defaultPolicy());

        Trading212ApiException exception = assertThrows(Trading212ApiException.class,
                () -> transport.get("/api/v0/equity/account/info", new TypeReference<AccountInfo>() {}));

        assertEquals(403, exception.statusCode());
        assertFalse(exception.isRateLimited());
        assertEquals(1, server.requests().size());
    }
}

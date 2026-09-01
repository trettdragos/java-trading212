package io.github.trettdragos.trading212.api;

import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.http.RetryPolicy;
import io.github.trettdragos.trading212.model.account.AccountCash;
import io.github.trettdragos.trading212.model.account.AccountInfo;
import io.github.trettdragos.trading212.testutil.StubServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AccountApiTest {

    private StubServer server;
    private AccountApi accountApi;

    @BeforeEach
    void setUp() {
        server = new StubServer();
        HttpTransport transport = new HttpTransport(server.baseUrl(), "test-api-key", HttpClient.newHttpClient(), RetryPolicy.none());
        accountApi = new AccountApi(transport);
    }

    @AfterEach
    void tearDown() {
        server.close();
    }

    @Test
    void getCash_parsesResponseAndSendsRawApiKeyAsAuthorizationHeader() {
        server.enqueue(200, """
                {"blocked": null, "free": 100.5, "invested": 250.25, "pieCash": 10, "ppl": 5.5, "result": 12.3, "total": 356.25}
                """);

        AccountCash cash = accountApi.getCash();

        assertNull(cash.blocked());
        assertEquals(new BigDecimal("100.5"), cash.free());
        assertEquals(new BigDecimal("356.25"), cash.total());

        StubServer.RecordedRequest request = server.requests().get(0);
        assertEquals("GET", request.method());
        assertEquals("/api/v0/equity/account/cash", request.path());
        assertEquals("test-api-key", request.authorization());
    }

    @Test
    void getInfo_parsesResponse() {
        server.enqueue(200, """
                {"currencyCode": "USD", "id": 42}
                """);

        AccountInfo info = accountApi.getInfo();

        assertEquals("USD", info.currencyCode());
        assertEquals(42L, info.id());
    }
}

package io.github.trettdragos.trading212.api;

import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.http.RetryPolicy;
import io.github.trettdragos.trading212.model.history.Transaction;
import io.github.trettdragos.trading212.pagination.PagedIterable;
import io.github.trettdragos.trading212.testutil.StubServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryApiTest {

    private StubServer server;
    private HistoryApi historyApi;

    @BeforeEach
    void setUp() {
        server = new StubServer();
        HttpTransport transport = new HttpTransport(server.baseUrl(), "test-api-key", HttpClient.newHttpClient(), RetryPolicy.none());
        historyApi = new HistoryApi(transport);
    }

    @AfterEach
    void tearDown() {
        server.close();
    }

    @Test
    void getTransactions_transparentlyFollowsNextPagePathAcrossPages() {
        server.enqueue(200, """
                {"items": [
                    {"amount": 100, "dateTime": "2026-01-01T00:00:00Z", "reference": "r1", "type": "DEPOSIT"}
                 ],
                 "nextPagePath": "/api/v0/history/transactions?limit=50&cursor=abc"}
                """);
        server.enqueue(200, """
                {"items": [
                    {"amount": -50, "dateTime": "2026-01-02T00:00:00Z", "reference": "r2", "type": "WITHDRAW"}
                 ],
                 "nextPagePath": null}
                """);

        PagedIterable<Transaction> transactions = historyApi.getTransactions();
        List<Transaction> all = transactions.stream().toList();

        assertEquals(2, all.size());
        assertEquals("r1", all.get(0).reference());
        assertEquals("r2", all.get(1).reference());
        assertEquals(2, server.requests().size());
        assertEquals("/api/v0/history/transactions?limit=50", server.requests().get(0).path());
        assertEquals("/api/v0/history/transactions?limit=50&cursor=abc", server.requests().get(1).path());
    }

    @Test
    void getOrders_treatsNextPagePathContainingLiteralNullAsTerminal() {
        server.enqueue(200, """
                {"items": [], "nextPagePath": "/api/v0/equity/history/orders?limit=50&cursor=null&time=null"}
                """);

        List<?> results = historyApi.getOrders().stream().toList();

        assertEquals(0, results.size());
        // Only the first request should have gone out; the "null" cursor must not trigger another page fetch.
        assertEquals(1, server.requests().size());
    }
}

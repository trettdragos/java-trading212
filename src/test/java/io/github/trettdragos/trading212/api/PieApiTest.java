package io.github.trettdragos.trading212.api;

import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.http.RetryPolicy;
import io.github.trettdragos.trading212.model.pie.CreatePieRequest;
import io.github.trettdragos.trading212.model.pie.DetailedPie;
import io.github.trettdragos.trading212.model.pie.DividendCashAction;
import io.github.trettdragos.trading212.model.pie.PieIcon;
import io.github.trettdragos.trading212.testutil.StubServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.time.OffsetDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PieApiTest {

    private StubServer server;
    private PieApi pieApi;

    @BeforeEach
    void setUp() {
        server = new StubServer();
        HttpTransport transport = new HttpTransport(server.baseUrl(), "test-api-key", HttpClient.newHttpClient(), RetryPolicy.none());
        pieApi = new PieApi(transport);
    }

    @AfterEach
    void tearDown() {
        server.close();
    }

    @Test
    void createPie_sendsInstrumentSharesAndParsesNestedSettings() {
        server.enqueue(200, """
                {"instruments": [
                    {"ticker": "AAPL_US_EQ", "currentShare": 0.5, "expectedShare": 0.5, "ownedQuantity": 1,
                     "result": {"investedValue": 100, "result": 5, "resultCoef": 0.05, "value": 105}, "issues": []}
                 ],
                 "settings": {
                    "id": 10, "name": "Tech", "icon": "Tech", "dividendCashAction": "REINVEST", "goal": 1000,
                    "initialInvestment": 100, "instrumentShares": {"AAPL_US_EQ": 1.0},
                    "creationDate": "2026-01-01T00:00:00Z", "endDate": "2027-01-01T00:00:00Z", "publicUrl": null
                 }}
                """);

        CreatePieRequest request = new CreatePieRequest(
                "Tech", PieIcon.Tech, DividendCashAction.REINVEST, new BigDecimal("1000"),
                OffsetDateTime.parse("2027-01-01T00:00:00Z"), Map.of("AAPL_US_EQ", BigDecimal.ONE));

        DetailedPie pie = pieApi.createPie(request);

        assertEquals("Tech", pie.settings().name());
        assertEquals(0, pie.settings().instrumentShares().get("AAPL_US_EQ").compareTo(BigDecimal.ONE));
        assertEquals(1, pie.instruments().size());

        StubServer.RecordedRequest recorded = server.requests().get(0);
        assertEquals("POST", recorded.method());
        assertEquals("/api/v0/equity/pies", recorded.path());
        assertTrue(recorded.body().contains("\"AAPL_US_EQ\":1"));
    }

    @Test
    void deletePie_sendsDeleteToPieById() {
        server.enqueue(200, "");

        pieApi.deletePie(10);

        StubServer.RecordedRequest recorded = server.requests().get(0);
        assertEquals("DELETE", recorded.method());
        assertEquals("/api/v0/equity/pies/10", recorded.path());
    }
}

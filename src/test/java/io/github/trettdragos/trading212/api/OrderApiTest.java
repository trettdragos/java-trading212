package io.github.trettdragos.trading212.api;

import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.http.RetryPolicy;
import io.github.trettdragos.trading212.model.order.Order;
import io.github.trettdragos.trading212.model.order.OrderStatus;
import io.github.trettdragos.trading212.model.order.OrderType;
import io.github.trettdragos.trading212.model.order.PlaceMarketOrderRequest;
import io.github.trettdragos.trading212.testutil.StubServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderApiTest {

    private StubServer server;
    private OrderApi orderApi;

    @BeforeEach
    void setUp() {
        server = new StubServer();
        HttpTransport transport = new HttpTransport(server.baseUrl(), "test-api-key", HttpClient.newHttpClient(), RetryPolicy.none());
        orderApi = new OrderApi(transport);
    }

    @AfterEach
    void tearDown() {
        server.close();
    }

    @Test
    void placeMarketOrder_sendsJsonBodyAndParsesOrder() {
        server.enqueue(200, """
                {"id": 1, "ticker": "AAPL_US_EQ", "type": "MARKET", "status": "NEW", "strategy": "QUANTITY",
                 "quantity": 2, "filledQuantity": 0, "value": 0, "filledValue": 0,
                 "limitPrice": 0, "stopPrice": 0, "creationTime": "2026-01-01T10:00:00Z"}
                """);

        Order order = orderApi.placeMarketOrder(new PlaceMarketOrderRequest("AAPL_US_EQ", new BigDecimal("2")));

        assertEquals(1L, order.id());
        assertEquals(OrderType.MARKET, order.type());
        assertEquals(OrderStatus.NEW, order.status());

        StubServer.RecordedRequest request = server.requests().get(0);
        assertEquals("POST", request.method());
        assertEquals("/api/v0/equity/orders/market", request.path());
        assertTrue(request.body().contains("\"ticker\":\"AAPL_US_EQ\""));
        assertTrue(request.body().contains("\"quantity\":2"));
    }

    @Test
    void cancelOrder_sendsDeleteToOrderById() {
        server.enqueue(200, "");

        orderApi.cancelOrder(99);

        StubServer.RecordedRequest request = server.requests().get(0);
        assertEquals("DELETE", request.method());
        assertEquals("/api/v0/equity/orders/99", request.path());
    }

    @Test
    void getOrder_unknownStatusFallsBackToUnknownEnumInsteadOfFailing() {
        server.enqueue(200, """
                {"id": 1, "ticker": "AAPL_US_EQ", "type": "MARKET", "status": "SOME_NEW_STATUS", "strategy": "QUANTITY",
                 "quantity": 2, "filledQuantity": 0, "value": 0, "filledValue": 0,
                 "limitPrice": 0, "stopPrice": 0, "creationTime": "2026-01-01T10:00:00Z"}
                """);

        Order order = orderApi.getOrder(1);

        assertEquals(OrderStatus.UNKNOWN, order.status());
    }
}

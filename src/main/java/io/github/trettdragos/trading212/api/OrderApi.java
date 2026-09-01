package io.github.trettdragos.trading212.api;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.model.order.Order;
import io.github.trettdragos.trading212.model.order.PlaceLimitOrderRequest;
import io.github.trettdragos.trading212.model.order.PlaceMarketOrderRequest;
import io.github.trettdragos.trading212.model.order.PlaceStopLimitOrderRequest;
import io.github.trettdragos.trading212.model.order.PlaceStopOrderRequest;

import java.util.List;

/**
 * Placing, inspecting, and cancelling working equity orders.
 *
 * @see <a href="https://docs.trading212.com/api">Trading212 Public API</a>
 */
public final class OrderApi {

    private static final String ORDERS = "/api/v0/equity/orders";
    private static final String ORDERS_LIMIT = ORDERS + "/limit";
    private static final String ORDERS_MARKET = ORDERS + "/market";
    private static final String ORDERS_STOP = ORDERS + "/stop";
    private static final String ORDERS_STOP_LIMIT = ORDERS + "/stop_limit";

    private final HttpTransport transport;

    public OrderApi(HttpTransport transport) {
        this.transport = transport;
    }

    /** All working (unfilled/partially-filled) orders. */
    public List<Order> getOrders() {
        return transport.get(ORDERS, new TypeReference<>() {});
    }

    /** A single working order by id. */
    public Order getOrder(long id) {
        return transport.get(ORDERS + "/" + id, new TypeReference<>() {});
    }

    /** Cancels a working order. */
    public void cancelOrder(long id) {
        transport.delete(ORDERS + "/" + id);
    }

    public Order placeMarketOrder(PlaceMarketOrderRequest request) {
        return transport.post(ORDERS_MARKET, request, new TypeReference<>() {});
    }

    public Order placeLimitOrder(PlaceLimitOrderRequest request) {
        return transport.post(ORDERS_LIMIT, request, new TypeReference<>() {});
    }

    public Order placeStopOrder(PlaceStopOrderRequest request) {
        return transport.post(ORDERS_STOP, request, new TypeReference<>() {});
    }

    public Order placeStopLimitOrder(PlaceStopLimitOrderRequest request) {
        return transport.post(ORDERS_STOP_LIMIT, request, new TypeReference<>() {});
    }
}

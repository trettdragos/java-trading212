package io.github.trettdragos.trading212.model.order;

import java.math.BigDecimal;

/**
 * @param ticker       the instrument to trade, e.g. "AAPL_US_EQ"
 * @param quantity     positive to buy, negative to sell
 * @param stopPrice    the order triggers as a market order once this price is reached
 * @param timeValidity how long the order should remain active
 */
public record PlaceStopOrderRequest(String ticker, BigDecimal quantity, BigDecimal stopPrice, TimeValidity timeValidity) {
}

package io.github.trettdragos.trading212.model.order;

import java.math.BigDecimal;

/**
 * @param ticker       the instrument to trade, e.g. "AAPL_US_EQ"
 * @param quantity     positive to buy, negative to sell
 * @param limitPrice   the order will only fill at this price or better
 * @param timeValidity how long the order should remain active
 */
public record PlaceLimitOrderRequest(String ticker, BigDecimal quantity, BigDecimal limitPrice, TimeValidity timeValidity) {
}

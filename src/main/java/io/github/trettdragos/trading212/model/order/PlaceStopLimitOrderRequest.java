package io.github.trettdragos.trading212.model.order;

import java.math.BigDecimal;

/**
 * @param ticker       the instrument to trade, e.g. "AAPL_US_EQ"
 * @param quantity     positive to buy, negative to sell
 * @param stopPrice    price at which the order triggers
 * @param limitPrice   once triggered, the order will only fill at this price or better
 * @param timeValidity how long the order should remain active
 */
public record PlaceStopLimitOrderRequest(
        String ticker, BigDecimal quantity, BigDecimal stopPrice, BigDecimal limitPrice, TimeValidity timeValidity) {
}

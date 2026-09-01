package io.github.trettdragos.trading212.model.order;

import java.math.BigDecimal;

/**
 * @param ticker   the instrument to trade, e.g. "AAPL_US_EQ"
 * @param quantity positive to buy, negative to sell
 */
public record PlaceMarketOrderRequest(String ticker, BigDecimal quantity) {
}

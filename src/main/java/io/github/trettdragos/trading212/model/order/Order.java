package io.github.trettdragos.trading212.model.order;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * A working or historical equity order. {@code limitPrice} and {@code stopPrice} are only
 * populated for order types that use them.
 */
public record Order(
        long id,
        String ticker,
        OrderType type,
        OrderStatus status,
        OrderStrategy strategy,
        BigDecimal quantity,
        BigDecimal filledQuantity,
        BigDecimal value,
        BigDecimal filledValue,
        BigDecimal limitPrice,
        BigDecimal stopPrice,
        OffsetDateTime creationTime) {
}

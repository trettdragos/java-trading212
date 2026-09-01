package io.github.trettdragos.trading212.model.portfolio;

import io.github.trettdragos.trading212.model.common.Device;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * An open position held in the account (outside of any Pie allocation accounting).
 *
 * @param ticker            the instrument, e.g. "AAPL_US_EQ"
 * @param quantity          total quantity held
 * @param pieQuantity       portion of {@code quantity} allocated to Pies
 * @param averagePrice      average price paid per unit
 * @param currentPrice      current market price per unit
 * @param ppl               unrealised profit/loss
 * @param fxPpl             FX-driven component of profit/loss, {@code null} when not applicable
 * @param maxBuy            maximum quantity that could currently be bought
 * @param maxSell           maximum quantity that could currently be sold
 * @param initialFillDate   when the position was first opened
 * @param frontend          what originated the initial fill
 */
public record Position(
        String ticker,
        BigDecimal quantity,
        BigDecimal pieQuantity,
        BigDecimal averagePrice,
        BigDecimal currentPrice,
        BigDecimal ppl,
        BigDecimal fxPpl,
        BigDecimal maxBuy,
        BigDecimal maxSell,
        OffsetDateTime initialFillDate,
        Device frontend) {
}

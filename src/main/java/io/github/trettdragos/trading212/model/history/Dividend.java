package io.github.trettdragos.trading212.model.history;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @param ticker               the instrument the dividend was paid for
 * @param amount               net amount paid, in the account's currency
 * @param amountInEuro         net amount paid, converted to EUR
 * @param grossAmountPerShare  gross dividend per share before any withholding
 * @param quantity             quantity held at the time of payout
 * @param paidOn               payout date
 * @param reference            unique reference for this payout
 * @param type                 tax classification of the payout
 */
public record Dividend(
        String ticker,
        BigDecimal amount,
        BigDecimal amountInEuro,
        BigDecimal grossAmountPerShare,
        BigDecimal quantity,
        OffsetDateTime paidOn,
        String reference,
        DividendType type) {
}

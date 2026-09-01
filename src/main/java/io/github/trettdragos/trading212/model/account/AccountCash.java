package io.github.trettdragos.trading212.model.account;

import java.math.BigDecimal;

/**
 * Cash balance breakdown for the account.
 *
 * @param blocked   cash tied up in unsettled activity, {@code null} when not applicable
 * @param free      cash available to invest or withdraw
 * @param invested  amount currently invested in open positions
 * @param pieCash   cash allocated to Pies but not yet invested
 * @param ppl       total profit/loss across open positions
 * @param result    realised result
 * @param total     total account value (free + invested + ppl)
 */
public record AccountCash(
        BigDecimal blocked,
        BigDecimal free,
        BigDecimal invested,
        BigDecimal pieCash,
        BigDecimal ppl,
        BigDecimal result,
        BigDecimal total) {
}

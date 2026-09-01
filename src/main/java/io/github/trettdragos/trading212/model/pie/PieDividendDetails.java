package io.github.trettdragos.trading212.model.pie;

import java.math.BigDecimal;

/**
 * @param gained     total dividends earned by the Pie
 * @param inCash     portion paid out to account cash
 * @param reinvested portion reinvested back into the Pie
 */
public record PieDividendDetails(BigDecimal gained, BigDecimal inCash, BigDecimal reinvested) {
}

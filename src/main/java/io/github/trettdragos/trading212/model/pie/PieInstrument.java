package io.github.trettdragos.trading212.model.pie;

import java.math.BigDecimal;
import java.util.List;

/**
 * @param ticker         the instrument, e.g. "AAPL_US_EQ"
 * @param currentShare   this instrument's current share of the Pie's value (0-1)
 * @param expectedShare  the target share configured for this instrument (0-1)
 * @param ownedQuantity  quantity of the instrument held via this Pie
 * @param result         profit/loss for this instrument within the Pie
 * @param issues         any problems affecting this instrument, e.g. it can no longer be traded
 */
public record PieInstrument(
        String ticker,
        BigDecimal currentShare,
        BigDecimal expectedShare,
        BigDecimal ownedQuantity,
        PieResult result,
        List<PieInstrumentIssue> issues) {
}

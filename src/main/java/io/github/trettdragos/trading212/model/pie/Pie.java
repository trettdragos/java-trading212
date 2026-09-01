package io.github.trettdragos.trading212.model.pie;

import java.math.BigDecimal;

/** Summary view of a Pie, as returned when listing all Pies. */
public record Pie(
        long id,
        BigDecimal cash,
        BigDecimal progress,
        PieDividendDetails dividendDetails,
        PieResult result,
        PieStatusGoal status) {
}

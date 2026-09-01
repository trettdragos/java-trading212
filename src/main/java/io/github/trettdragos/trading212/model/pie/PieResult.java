package io.github.trettdragos.trading212.model.pie;

import java.math.BigDecimal;

public record PieResult(BigDecimal investedValue, BigDecimal result, BigDecimal resultCoef, BigDecimal value) {
}

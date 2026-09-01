package io.github.trettdragos.trading212.model.pie;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * @param instrumentShares target allocation by ticker (0-1 each), {@code null} if not configured
 * @param goal             target cash value for the Pie, or {@code 0} if it has no goal
 * @param endDate          target date to reach the goal by
 * @param publicUrl        public sharing link, {@code null} if the Pie isn't shared
 */
public record PieSettings(
        long id,
        String name,
        PieIcon icon,
        DividendCashAction dividendCashAction,
        BigDecimal goal,
        BigDecimal initialInvestment,
        Map<String, BigDecimal> instrumentShares,
        OffsetDateTime creationDate,
        OffsetDateTime endDate,
        String publicUrl) {
}

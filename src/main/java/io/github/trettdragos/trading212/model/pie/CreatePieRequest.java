package io.github.trettdragos.trading212.model.pie;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

/**
 * @param name               display name for the Pie
 * @param icon               icon to show in the app
 * @param dividendCashAction what to do with dividends earned by the Pie
 * @param goal               target cash value, or {@code BigDecimal.ZERO} for no goal
 * @param endDate            target date to reach the goal by
 * @param instrumentShares   target allocation by ticker; values must sum to 1
 */
public record CreatePieRequest(
        String name,
        PieIcon icon,
        DividendCashAction dividendCashAction,
        BigDecimal goal,
        OffsetDateTime endDate,
        Map<String, BigDecimal> instrumentShares) {
}

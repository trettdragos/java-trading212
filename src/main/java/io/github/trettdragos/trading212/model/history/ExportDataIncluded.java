package io.github.trettdragos.trading212.model.history;

public record ExportDataIncluded(
        boolean includeDividends, boolean includeInterest, boolean includeOrders, boolean includeTransactions) {
}

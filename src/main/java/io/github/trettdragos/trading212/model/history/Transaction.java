package io.github.trettdragos.trading212.model.history;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Transaction(BigDecimal amount, OffsetDateTime dateTime, String reference, TransactionType type) {
}

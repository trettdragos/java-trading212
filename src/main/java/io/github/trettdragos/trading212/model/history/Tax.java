package io.github.trettdragos.trading212.model.history;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Tax(String fillId, TaxName name, BigDecimal quantity, OffsetDateTime timeCharged) {
}

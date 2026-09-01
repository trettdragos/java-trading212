package io.github.trettdragos.trading212.model.history;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum TransactionType {
    DEPOSIT,
    FEE,
    TRANSFER,
    WITHDRAW,
    @JsonEnumDefaultValue
    UNKNOWN
}

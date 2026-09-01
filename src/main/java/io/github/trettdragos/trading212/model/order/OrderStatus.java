package io.github.trettdragos.trading212.model.order;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum OrderStatus {
    CANCELLED,
    CANCELLING,
    CONFIRMED,
    FILLED,
    LOCAL,
    NEW,
    PARTIALLY_FILLED,
    REJECTED,
    REPLACED,
    REPLACING,
    UNCONFIRMED,
    @JsonEnumDefaultValue
    UNKNOWN
}

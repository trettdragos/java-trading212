package io.github.trettdragos.trading212.model.order;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum OrderType {
    MARKET,
    LIMIT,
    STOP,
    STOP_LIMIT,
    @JsonEnumDefaultValue
    UNKNOWN
}

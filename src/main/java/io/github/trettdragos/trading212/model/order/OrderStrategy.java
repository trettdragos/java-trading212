package io.github.trettdragos.trading212.model.order;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Whether an order was sized by share quantity or by cash value. */
public enum OrderStrategy {
    QUANTITY,
    VALUE,
    @JsonEnumDefaultValue
    UNKNOWN
}

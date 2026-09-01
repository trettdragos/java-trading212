package io.github.trettdragos.trading212.model.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Whether a fill happened on a trading venue or over-the-counter. */
public enum FillType {
    TOTV,
    OTC,
    @JsonEnumDefaultValue
    UNKNOWN
}

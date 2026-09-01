package io.github.trettdragos.trading212.model.metadata;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum InstrumentType {
    CORPACT,
    CRYPTO,
    CRYPTOCURRENCY,
    CVR,
    ETF,
    FOREX,
    FUTURES,
    INDEX,
    STOCK,
    WARRANT,
    @JsonEnumDefaultValue
    UNKNOWN
}

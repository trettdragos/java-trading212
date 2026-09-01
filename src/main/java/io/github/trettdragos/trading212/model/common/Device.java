package io.github.trettdragos.trading212.model.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Where an order/fill originated from. */
public enum Device {
    ANDROID,
    API,
    AUTOINVEST,
    IOS,
    SYSTEM,
    WEB,
    @JsonEnumDefaultValue
    UNKNOWN
}

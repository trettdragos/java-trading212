package io.github.trettdragos.trading212.model.history;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum TaxName {
    COMMISSION_TURNOVER,
    CURRENCY_CONVERSION_FEE,
    FINRA_FEE,
    FRENCH_TRANSACTION_TAX,
    PTM_LEVY,
    STAMP_DUTY_RESERVE_TAX,
    STAMP_DUTY,
    TRANSACTION_FEE,
    @JsonEnumDefaultValue
    UNKNOWN
}

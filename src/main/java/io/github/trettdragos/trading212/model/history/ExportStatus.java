package io.github.trettdragos.trading212.model.history;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum ExportStatus {
    Canceled,
    Failed,
    Finished,
    Processing,
    Queued,
    Running,
    @JsonEnumDefaultValue
    UNKNOWN
}

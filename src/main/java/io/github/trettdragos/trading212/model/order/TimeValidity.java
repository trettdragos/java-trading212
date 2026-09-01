package io.github.trettdragos.trading212.model.order;

/** How long a resting order (limit/stop/stop-limit) remains active. */
public enum TimeValidity {
    /** Valid until the end of the current trading day. */
    DAY,
    /** Good-till-cancelled. */
    GTC
}

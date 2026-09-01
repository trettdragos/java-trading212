package io.github.trettdragos.trading212.model.metadata;

/** A single open/close boundary within an exchange's working schedule. */
public record TimeEvent(String date, String type) {
}

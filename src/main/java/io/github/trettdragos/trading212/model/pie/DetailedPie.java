package io.github.trettdragos.trading212.model.pie;

import java.util.List;

/** Full view of a single Pie, as returned when fetching, creating, or updating one Pie by id. */
public record DetailedPie(List<PieInstrument> instruments, PieSettings settings) {
}

package io.github.trettdragos.trading212.model.pie;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Progress of a Pie towards its goal, if it has one. */
public enum PieStatusGoal {
    AHEAD,
    ON_TRACK,
    BEHIND,
    @JsonEnumDefaultValue
    UNKNOWN
}

package io.github.trettdragos.trading212.model.metadata;

import java.util.List;

public record WorkingSchedule(long id, List<TimeEvent> timeEvents) {
}

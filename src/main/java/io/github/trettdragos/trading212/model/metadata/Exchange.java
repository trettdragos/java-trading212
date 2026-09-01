package io.github.trettdragos.trading212.model.metadata;

import java.util.List;

public record Exchange(long id, String name, List<WorkingSchedule> workingSchedules) {
}

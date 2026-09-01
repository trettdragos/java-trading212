package io.github.trettdragos.trading212.model.history;

import java.time.OffsetDateTime;

/**
 * @param timeFrom     start of the reporting window (inclusive)
 * @param timeTo       end of the reporting window (exclusive)
 * @param dataIncluded which categories of activity to include in the CSV
 */
public record ExportRequest(OffsetDateTime timeFrom, OffsetDateTime timeTo, ExportDataIncluded dataIncluded) {
}

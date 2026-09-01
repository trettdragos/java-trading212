package io.github.trettdragos.trading212.model.history;

import java.time.OffsetDateTime;

/**
 * A CSV export job, as returned when listing previously requested exports.
 *
 * @param downloadLink populated once {@code status} is {@link ExportStatus#Finished}
 */
public record Export(
        long reportId,
        ExportStatus status,
        String downloadLink,
        OffsetDateTime timeFrom,
        OffsetDateTime timeTo,
        ExportDataIncluded dataIncluded) {
}

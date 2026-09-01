package io.github.trettdragos.trading212.model.history;

/** Acknowledgement returned immediately after requesting a CSV export; the export itself is generated asynchronously. */
public record ExportResponse(long reportId) {
}

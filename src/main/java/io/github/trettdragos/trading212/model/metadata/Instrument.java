package io.github.trettdragos.trading212.model.metadata;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @param ticker            Trading212's identifier for the instrument, e.g. "AAPL_US_EQ"
 * @param isin              International Securities Identification Number
 * @param name              full instrument name
 * @param shortname         abbreviated name, {@code null} if none is set
 * @param type              instrument class
 * @param currencyCode      ISO 4217 currency the instrument is priced in
 * @param minTradeQuantity  smallest quantity that can be traded
 * @param maxOpenQuantity   largest quantity that can be held open
 * @param addedOn           when Trading212 added this instrument
 * @param workingScheduleId links to {@link Exchange#workingSchedules()} for trading hours
 */
public record Instrument(
        String ticker,
        String isin,
        String name,
        String shortname,
        InstrumentType type,
        String currencyCode,
        BigDecimal minTradeQuantity,
        BigDecimal maxOpenQuantity,
        OffsetDateTime addedOn,
        long workingScheduleId) {
}

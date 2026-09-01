package io.github.trettdragos.trading212.api;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.model.metadata.Exchange;
import io.github.trettdragos.trading212.model.metadata.Instrument;

import java.util.List;

/**
 * Reference data: tradable instruments and the exchanges/working schedules behind them. These
 * endpoints change rarely; Trading212 asks integrators to cache the results rather than polling.
 *
 * @see <a href="https://docs.trading212.com/api">Trading212 Public API</a>
 */
public final class MetadataApi {

    private static final String EXCHANGES = "/api/v0/equity/metadata/exchanges";
    private static final String INSTRUMENTS = "/api/v0/equity/metadata/instruments";

    private final HttpTransport transport;

    public MetadataApi(HttpTransport transport) {
        this.transport = transport;
    }

    /** All exchanges Trading212 lists instruments on, with their working schedules. */
    public List<Exchange> getExchanges() {
        return transport.get(EXCHANGES, new TypeReference<>() {});
    }

    /** Every instrument tradable through the account. */
    public List<Instrument> getInstruments() {
        return transport.get(INSTRUMENTS, new TypeReference<>() {});
    }
}

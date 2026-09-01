package io.github.trettdragos.trading212.api;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.model.portfolio.Position;

import java.util.List;

/**
 * The account's currently open positions.
 *
 * @see <a href="https://docs.trading212.com/api">Trading212 Public API</a>
 */
public final class PortfolioApi {

    private static final String PORTFOLIO = "/api/v0/equity/portfolio";

    private final HttpTransport transport;

    public PortfolioApi(HttpTransport transport) {
        this.transport = transport;
    }

    /** All currently open positions. */
    public List<Position> getPositions() {
        return transport.get(PORTFOLIO, new TypeReference<>() {});
    }

    /** The open position for a single instrument. */
    public Position getPosition(String ticker) {
        return transport.get(PORTFOLIO + "/" + ticker, new TypeReference<>() {});
    }
}

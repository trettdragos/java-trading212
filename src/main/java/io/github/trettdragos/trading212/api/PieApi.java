package io.github.trettdragos.trading212.api;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.model.pie.CreatePieRequest;
import io.github.trettdragos.trading212.model.pie.DetailedPie;
import io.github.trettdragos.trading212.model.pie.Pie;

import java.util.List;

/**
 * Creating and managing Pies (Trading212's basket-of-instruments feature).
 *
 * @see <a href="https://docs.trading212.com/api">Trading212 Public API</a>
 */
public final class PieApi {

    private static final String PIES = "/api/v0/equity/pies";

    private final HttpTransport transport;

    public PieApi(HttpTransport transport) {
        this.transport = transport;
    }

    /** Summary of every Pie in the account. */
    public List<Pie> getPies() {
        return transport.get(PIES, new TypeReference<>() {});
    }

    /** Full detail (instruments, settings) for a single Pie. */
    public DetailedPie getPie(long id) {
        return transport.get(PIES + "/" + id, new TypeReference<>() {});
    }

    public DetailedPie createPie(CreatePieRequest request) {
        return transport.post(PIES, request, new TypeReference<>() {});
    }

    public DetailedPie updatePie(long id, CreatePieRequest request) {
        return transport.post(PIES + "/" + id, request, new TypeReference<>() {});
    }

    public void deletePie(long id) {
        transport.delete(PIES + "/" + id);
    }
}

package io.github.trettdragos.trading212.api;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.model.history.Dividend;
import io.github.trettdragos.trading212.model.history.Export;
import io.github.trettdragos.trading212.model.history.ExportRequest;
import io.github.trettdragos.trading212.model.history.ExportResponse;
import io.github.trettdragos.trading212.model.history.HistoryOrder;
import io.github.trettdragos.trading212.model.history.Transaction;
import io.github.trettdragos.trading212.pagination.Page;
import io.github.trettdragos.trading212.pagination.PagedIterable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Historical account activity: past orders, dividend payouts, cash transactions, and CSV export
 * jobs. The order/dividend/transaction endpoints are cursor-paginated by Trading212; the returned
 * {@link PagedIterable} fetches pages lazily as it's consumed.
 *
 * @see <a href="https://docs.trading212.com/api">Trading212 Public API</a>
 */
public final class HistoryApi {

    private static final String ORDERS = "/api/v0/equity/history/orders";
    private static final String DIVIDENDS = "/api/v0/history/dividends";
    private static final String TRANSACTIONS = "/api/v0/history/transactions";
    private static final String EXPORTS = "/api/v0/history/exports";

    private static final int PAGE_SIZE = 50;

    private final HttpTransport transport;

    public HistoryApi(HttpTransport transport) {
        this.transport = transport;
    }

    /** Historical orders for a single instrument. */
    public PagedIterable<HistoryOrder> getOrders(String ticker) {
        String path = pagedPath(ORDERS, ticker);
        return new PagedIterable<>(path, p -> transport.get(p, new TypeReference<Page<HistoryOrder>>() {}));
    }

    /** All historical orders across every instrument. */
    public PagedIterable<HistoryOrder> getOrders() {
        return getOrders(null);
    }

    /** Dividend payouts for a single instrument. */
    public PagedIterable<Dividend> getDividends(String ticker) {
        String path = pagedPath(DIVIDENDS, ticker);
        return new PagedIterable<>(path, p -> transport.get(p, new TypeReference<Page<Dividend>>() {}));
    }

    /** All dividend payouts across every instrument. */
    public PagedIterable<Dividend> getDividends() {
        return getDividends(null);
    }

    /** Cash transactions: deposits, withdrawals, transfers, and fees. */
    public PagedIterable<Transaction> getTransactions() {
        String path = pagedPath(TRANSACTIONS, null);
        return new PagedIterable<>(path, p -> transport.get(p, new TypeReference<Page<Transaction>>() {}));
    }

    /** Previously requested CSV exports and their current status. */
    public List<Export> getExports() {
        return transport.get(EXPORTS, new TypeReference<>() {});
    }

    /** Kicks off a new CSV export job; the file is generated asynchronously, poll {@link #getExports()} for its {@code downloadLink}. */
    public ExportResponse requestExport(ExportRequest request) {
        return transport.post(EXPORTS, request, new TypeReference<>() {});
    }

    private String pagedPath(String path, String ticker) {
        StringBuilder sb = new StringBuilder(path).append("?limit=").append(PAGE_SIZE);
        if (ticker != null && !ticker.isBlank()) {
            sb.append("&ticker=").append(URLEncoder.encode(ticker, StandardCharsets.UTF_8));
        }
        return sb.toString();
    }
}

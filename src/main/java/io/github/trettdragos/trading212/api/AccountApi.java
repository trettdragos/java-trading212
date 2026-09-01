package io.github.trettdragos.trading212.api;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.model.account.AccountCash;
import io.github.trettdragos.trading212.model.account.AccountInfo;

/**
 * Account-level data: cash balance and basic account identification.
 *
 * @see <a href="https://docs.trading212.com/api">Trading212 Public API</a>
 */
public final class AccountApi {

    private static final String CASH = "/api/v0/equity/account/cash";
    private static final String INFO = "/api/v0/equity/account/info";

    private final HttpTransport transport;

    public AccountApi(HttpTransport transport) {
        this.transport = transport;
    }

    /** Current cash balance breakdown for the account. */
    public AccountCash getCash() {
        return transport.get(CASH, new TypeReference<>() {});
    }

    /** Basic account identification: id and base currency. */
    public AccountInfo getInfo() {
        return transport.get(INFO, new TypeReference<>() {});
    }
}

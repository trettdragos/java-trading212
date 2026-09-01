package io.github.trettdragos.trading212;

import io.github.trettdragos.trading212.api.AccountApi;
import io.github.trettdragos.trading212.api.HistoryApi;
import io.github.trettdragos.trading212.api.MetadataApi;
import io.github.trettdragos.trading212.api.OrderApi;
import io.github.trettdragos.trading212.api.PieApi;
import io.github.trettdragos.trading212.api.PortfolioApi;
import io.github.trettdragos.trading212.http.HttpTransport;
import io.github.trettdragos.trading212.http.RetryPolicy;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Objects;

/**
 * Entry point for the Trading212 Public API client. Create one per API key with {@link #builder}
 * and reuse it &mdash; it and every resource it exposes are stateless and thread-safe.
 *
 * <pre>{@code
 * Trading212Client client = Trading212Client.builder()
 *         .environment(Trading212Environment.DEMO)
 *         .apiKey(System.getenv("TRADING212_API_KEY"))
 *         .build();
 *
 * AccountCash cash = client.account().getCash();
 * for (Position position : client.portfolio().getPositions()) {
 *     System.out.println(position.ticker() + ": " + position.quantity());
 * }
 * }</pre>
 */
public final class Trading212Client {

    private final AccountApi account;
    private final PortfolioApi portfolio;
    private final OrderApi orders;
    private final PieApi pies;
    private final MetadataApi metadata;
    private final HistoryApi history;

    private Trading212Client(Builder builder) {
        HttpTransport transport = new HttpTransport(builder.environment.baseUrl(), builder.apiKey, builder.httpClient, builder.retryPolicy);
        this.account = new AccountApi(transport);
        this.portfolio = new PortfolioApi(transport);
        this.orders = new OrderApi(transport);
        this.pies = new PieApi(transport);
        this.metadata = new MetadataApi(transport);
        this.history = new HistoryApi(transport);
    }

    public static Builder builder() {
        return new Builder();
    }

    public AccountApi account() {
        return account;
    }

    public PortfolioApi portfolio() {
        return portfolio;
    }

    public OrderApi orders() {
        return orders;
    }

    public PieApi pies() {
        return pies;
    }

    public MetadataApi metadata() {
        return metadata;
    }

    public HistoryApi history() {
        return history;
    }

    public static final class Builder {
        private Trading212Environment environment;
        private String apiKey;
        private HttpClient httpClient;
        private RetryPolicy retryPolicy = RetryPolicy.defaultPolicy();

        private Builder() {
        }

        /** Which of the two Trading212 environments to talk to. Required. */
        public Builder environment(Trading212Environment environment) {
            this.environment = environment;
            return this;
        }

        /**
         * The API key generated from Settings &gt; API (Beta) in the Trading212 app. Demo and live
         * keys are separate; use the one matching {@link #environment}. Required.
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /** Overrides the underlying {@link HttpClient}. Defaults to one with a 10s connect timeout. */
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * Controls how HTTP 429 (rate limited) responses are retried. Defaults to
         * {@link RetryPolicy#defaultPolicy()}; pass {@link RetryPolicy#none()} to fail fast instead.
         */
        public Builder retryPolicy(RetryPolicy retryPolicy) {
            this.retryPolicy = Objects.requireNonNull(retryPolicy, "retryPolicy");
            return this;
        }

        public Trading212Client build() {
            Objects.requireNonNull(environment, "environment must be set");
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException("apiKey must be set");
            }
            if (httpClient == null) {
                httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
            }
            return new Trading212Client(this);
        }
    }
}

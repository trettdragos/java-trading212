package io.github.trettdragos.trading212;

/**
 * The two account environments exposed by Trading212's Public API. Each has its own API key,
 * generated separately from the account's Settings &gt; API (Beta) page.
 */
public enum Trading212Environment {
    /** Paper trading / practice account. No real money is involved. */
    DEMO("https://demo.trading212.com"),
    /** Live trading with real money. */
    LIVE("https://live.trading212.com");

    private final String baseUrl;

    Trading212Environment(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String baseUrl() {
        return baseUrl;
    }
}

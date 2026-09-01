# java-trading212

An unofficial Java client for [Trading212](https://www.trading212.com)'s Public (Equity) API — the
official REST API for the General Invest and Stocks & Shares ISA accounts, covering account cash,
open positions, orders, Pies, instrument metadata, and historical activity.

This project is not affiliated with, endorsed by, or supported by Trading212.

## Requirements

- Java 21+ (a Java 17-compatible release is planned; the codebase intentionally avoids Java 21-only
  language features to keep that backport simple)
- A Trading212 API key, generated from **Settings > API (Beta)** in the app. Demo (paper trading)
  and live accounts each have their own key.

## Install

Not yet published to Maven Central. For now, build and install it locally:

```bash
mvn install
```

```xml
<dependency>
    <groupId>io.github.trettdragos</groupId>
    <artifactId>trading212-client</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Usage

```java
Trading212Client client = Trading212Client.builder()
        .environment(Trading212Environment.DEMO) // or LIVE
        .apiKey(System.getenv("TRADING212_API_KEY"))
        .build();

AccountCash cash = client.account().getCash();
System.out.println("Free cash: " + cash.free());

for (Position position : client.portfolio().getPositions()) {
    System.out.println(position.ticker() + " x" + position.quantity());
}

Order order = client.orders().placeMarketOrder(
        new PlaceMarketOrderRequest("AAPL_US_EQ", new BigDecimal("1")));

// Cursor-paginated endpoints return a lazily-fetched PagedIterable
for (Dividend dividend : client.history().getDividends()) {
    System.out.println(dividend.ticker() + ": " + dividend.amount());
}
```

### Rate limiting

Trading212 rate-limits every endpoint per account, regardless of API key or IP. By default the
client retries HTTP 429 responses with backoff (honoring a `Retry-After` header when Trading212
sends one). Configure this via `RetryPolicy`:

```java
Trading212Client.builder()
        .environment(Trading212Environment.LIVE)
        .apiKey(apiKey)
        .retryPolicy(new RetryPolicy(10, Duration.ofSeconds(2), Duration.ofSeconds(60)))
        // or RetryPolicy.none() to fail fast on 429 instead
        .build();
```

### Errors

Non-2xx responses throw `Trading212ApiException` (status code + raw response body). Transport-level
failures (network errors, unparseable responses) throw `Trading212Exception`.

## API coverage

| Resource                    | Client accessor         |
|------------------------------|--------------------------|
| Account cash & info           | `client.account()`      |
| Open positions                | `client.portfolio()`    |
| Orders (place/get/cancel)     | `client.orders()`       |
| Pies (create/get/update/delete) | `client.pies()`        |
| Instruments & exchanges       | `client.metadata()`     |
| Historical orders/dividends/transactions & CSV exports | `client.history()` |

## Building

```bash
mvn test
```

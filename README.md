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

Published to [GitHub Packages](https://github.com/trettdragos/java-trading212/packages) on every
GitHub Release (see [Releasing](#releasing) below). GitHub Packages requires authentication to
*read* Maven artifacts even from a public repo, so consumers need a GitHub personal access token
with the `read:packages` scope.

Add the repository and dependency to your project's `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/trettdragos/java-trading212</url>
    </repository>
</repositories>

<dependency>
    <groupId>io.github.trettdragos</groupId>
    <artifactId>trading212-client</artifactId>
    <version><!-- a published release version, e.g. 1.0.0 --></version>
</dependency>
```

Then add a `<server>` entry for the `github` id to `~/.m2/settings.xml` (do not commit this file):

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```

Alternatively, to build and install a local copy without any of the above:

```bash
mvn install
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

### Supplying the API key via a system property

Instead of `.apiKey(...)`, the key can be supplied through the `trading212.apiKey` system property
(`Trading212Client.API_KEY_PROPERTY`). This is handy for apps that read secrets from the
environment at startup and forward them as JVM properties rather than passing them through
application code:

```bash
java -Dtrading212.apiKey="$TRADING212_API_KEY" -jar app.jar
```

```java
// or programmatically, early in main:
System.setProperty(Trading212Client.API_KEY_PROPERTY, System.getenv("TRADING212_API_KEY"));

Trading212Client client = Trading212Client.builder()
        .environment(Trading212Environment.LIVE)
        .build(); // apiKey() omitted; falls back to the system property
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

## Releasing

Publishing to GitHub Packages is handled by [`.github/workflows/release.yml`](.github/workflows/release.yml),
triggered whenever a GitHub Release is published:

1. Draft a new [GitHub Release](https://github.com/trettdragos/java-trading212/releases/new) with a
   tag in the form `vX.Y.Z` (e.g. `v1.0.0`).
2. Publish it. The workflow sets the Maven project version to `X.Y.Z` (stripping the `v`), runs the
   test suite, and deploys the jar (plus a sources jar) to GitHub Packages using the repo's built-in
   `GITHUB_TOKEN` — no manual version bump or secrets setup needed.

`pom.xml` itself stays on its `-SNAPSHOT` version between releases; the release tag is the only
source of truth for published version numbers.

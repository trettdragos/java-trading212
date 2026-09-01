package io.github.trettdragos.trading212.http;

import java.time.Duration;

/**
 * Controls how {@link HttpTransport} reacts to HTTP 429 (rate limited) responses. Trading212
 * enforces per-endpoint rate limits regardless of which API key or IP address is used, so bursts
 * of requests routinely get throttled; retrying with backoff is the expected way to handle this.
 *
 * @param maxRetries      maximum number of retry attempts after the initial request (0 disables retrying)
 * @param initialBackoff  delay before the first retry, used when Trading212 does not send a {@code Retry-After} header
 * @param maxBackoff      upper bound applied to both the exponential backoff and any {@code Retry-After} value
 */
public record RetryPolicy(int maxRetries, Duration initialBackoff, Duration maxBackoff) {

    public static RetryPolicy defaultPolicy() {
        return new RetryPolicy(5, Duration.ofSeconds(1), Duration.ofSeconds(30));
    }

    public static RetryPolicy none() {
        return new RetryPolicy(0, Duration.ZERO, Duration.ZERO);
    }

    public RetryPolicy {
        if (maxRetries < 0) {
            throw new IllegalArgumentException("maxRetries must be >= 0");
        }
    }

    Duration backoffFor(int attempt) {
        long millis = initialBackoff.toMillis() * (1L << Math.min(attempt, 30));
        return Duration.ofMillis(Math.min(millis, maxBackoff.toMillis()));
    }
}

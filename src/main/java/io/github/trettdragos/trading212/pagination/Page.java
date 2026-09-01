package io.github.trettdragos.trading212.pagination;

import java.util.List;

/**
 * Raw shape of a paginated Trading212 response: a page of items plus a server-provided path
 * (already including its own query string) to fetch the next page, or {@code null} on the last page.
 */
public record Page<T>(List<T> items, String nextPagePath) {
}

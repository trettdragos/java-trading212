package io.github.trettdragos.trading212;

/**
 * Raised when Trading212 responds with a non-2xx HTTP status. Carries the status code and raw
 * response body so callers can decide how to react (e.g. distinguish 400 validation errors from
 * 403 permission errors).
 */
public class Trading212ApiException extends Trading212Exception {

    private final int statusCode;
    private final String responseBody;

    public Trading212ApiException(int statusCode, String responseBody) {
        super("Trading212 API request failed with status " + statusCode
                + (responseBody == null || responseBody.isBlank() ? "" : ": " + responseBody));
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    /** The HTTP status code returned by Trading212. */
    public int statusCode() {
        return statusCode;
    }

    /** The raw (unparsed) response body, if any. */
    public String responseBody() {
        return responseBody;
    }

    /** True when Trading212 rejected the request because of its rate limiter (HTTP 429). */
    public boolean isRateLimited() {
        return statusCode == 429;
    }
}

package io.github.trettdragos.trading212;

/**
 * Base exception for all failures raised by this client: transport failures, non-2xx API
 * responses, and response bodies that don't match the expected shape.
 */
public class Trading212Exception extends RuntimeException {

    public Trading212Exception(String message) {
        super(message);
    }

    public Trading212Exception(String message, Throwable cause) {
        super(message, cause);
    }
}

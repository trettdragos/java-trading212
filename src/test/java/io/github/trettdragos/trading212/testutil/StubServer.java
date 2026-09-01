package io.github.trettdragos.trading212.testutil;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Minimal HTTP stub built on the JDK's own {@link HttpServer}, used in tests instead of a mocking
 * library. Responses are queued in order and handed out one per incoming request; every received
 * request is recorded for assertions.
 */
public final class StubServer implements AutoCloseable {

    public record RecordedRequest(String method, String path, String authorization, String body) {
    }

    public record QueuedResponse(int status, String body, Map<String, String> headers) {
    }

    private final HttpServer server;
    private final Deque<QueuedResponse> responses = new ArrayDeque<>();
    private final List<RecordedRequest> requests = new ArrayList<>();

    public StubServer() {
        try {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        } catch (IOException e) {
            throw new UncheckedStubException(e);
        }
        server.createContext("/", this::handle);
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();
    }

    public String baseUrl() {
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    public void enqueue(int status, String body) {
        responses.addLast(new QueuedResponse(status, body, Map.of()));
    }

    public void enqueue(int status, String body, Map<String, String> headers) {
        responses.addLast(new QueuedResponse(status, body, headers));
    }

    public List<RecordedRequest> requests() {
        return List.copyOf(requests);
    }

    private synchronized void handle(HttpExchange exchange) throws IOException {
        String body = new String(readAll(exchange.getRequestBody()), StandardCharsets.UTF_8);
        String path = exchange.getRequestURI().getRawPath()
                + (exchange.getRequestURI().getRawQuery() == null ? "" : "?" + exchange.getRequestURI().getRawQuery());
        requests.add(new RecordedRequest(
                exchange.getRequestMethod(),
                path,
                exchange.getRequestHeaders().getFirst("Authorization"),
                body));

        QueuedResponse response = responses.isEmpty() ? new QueuedResponse(500, "no stubbed response", Map.of()) : responses.removeFirst();
        byte[] payload = response.body().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        response.headers().forEach((name, value) -> exchange.getResponseHeaders().add(name, value));
        exchange.sendResponseHeaders(response.status(), payload.length == 0 ? -1 : payload.length);
        if (payload.length > 0) {
            exchange.getResponseBody().write(payload);
        }
        exchange.close();
    }

    private static byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        in.transferTo(out);
        return out.toByteArray();
    }

    @Override
    public void close() {
        server.stop(0);
    }

    private static final class UncheckedStubException extends RuntimeException {
        UncheckedStubException(Throwable cause) {
            super(cause);
        }
    }
}

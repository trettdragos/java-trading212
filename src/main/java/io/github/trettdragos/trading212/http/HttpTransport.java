package io.github.trettdragos.trading212.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.trettdragos.trading212.Trading212ApiException;
import io.github.trettdragos.trading212.Trading212Exception;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

/**
 * Handles the mechanics shared by every Trading212 API resource: authentication, URL/query
 * building, JSON (de)serialization, and retrying HTTP 429 responses. Not part of the public API
 * surface directly &mdash; callers go through {@link io.github.trettdragos.trading212.Trading212Client}
 * and the per-resource {@code *Api} classes.
 */
public final class HttpTransport {

    private final String baseUrl;
    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final RetryPolicy retryPolicy;

    public HttpTransport(String baseUrl, String apiKey, HttpClient httpClient, RetryPolicy retryPolicy) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.httpClient = httpClient;
        this.retryPolicy = retryPolicy;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
    }

    public ObjectMapper objectMapper() {
        return objectMapper;
    }

    public <T> T get(String path, TypeReference<T> responseType) {
        return get(path, Map.of(), responseType);
    }

    public <T> T get(String path, Map<String, String> query, TypeReference<T> responseType) {
        HttpRequest request = requestBuilder(path, query).GET().build();
        return execute(request, responseType);
    }

    public <T> T post(String path, Object body, TypeReference<T> responseType) {
        HttpRequest request = requestBuilder(path, Map.of())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(writeJson(body), StandardCharsets.UTF_8))
                .build();
        return execute(request, responseType);
    }

    public void delete(String path) {
        HttpRequest request = requestBuilder(path, Map.of()).DELETE().build();
        execute(request, null);
    }

    private HttpRequest.Builder requestBuilder(String path, Map<String, String> query) {
        return HttpRequest.newBuilder(buildUri(path, query))
                .header("Authorization", apiKey)
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(60));
    }

    private URI buildUri(String path, Map<String, String> query) {
        StringBuilder url = new StringBuilder(baseUrl).append(path);
        if (!query.isEmpty()) {
            StringBuilder qs = new StringBuilder();
            for (Map.Entry<String, String> entry : query.entrySet()) {
                if (!qs.isEmpty()) {
                    qs.append('&');
                }
                qs.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                        .append('=')
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            }
            url.append('?').append(qs);
        }
        return URI.create(url.toString());
    }

    private <T> T execute(HttpRequest request, TypeReference<T> responseType) {
        int attempt = 0;
        while (true) {
            HttpResponse<String> response = send(request);
            int status = response.statusCode();

            if (status >= 200 && status < 300) {
                return readBody(response.body(), responseType);
            }

            if (status == 429 && attempt < retryPolicy.maxRetries()) {
                sleep(retryDelay(response, attempt));
                attempt++;
                continue;
            }

            throw new Trading212ApiException(status, response.body());
        }
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new Trading212Exception("Request to " + request.uri() + " failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new Trading212Exception("Request to " + request.uri() + " was interrupted", e);
        }
    }

    private Duration retryDelay(HttpResponse<String> response, int attempt) {
        return response.headers().firstValue("Retry-After")
                .map(this::parseRetryAfter)
                .orElseGet(() -> retryPolicy.backoffFor(attempt));
    }

    private Duration parseRetryAfter(String value) {
        try {
            long seconds = Long.parseLong(value.trim());
            Duration parsed = Duration.ofSeconds(Math.max(seconds, 0));
            return parsed.compareTo(retryPolicy.maxBackoff()) > 0 ? retryPolicy.maxBackoff() : parsed;
        } catch (NumberFormatException e) {
            return retryPolicy.initialBackoff();
        }
    }

    private void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new Trading212Exception("Interrupted while waiting to retry a rate-limited request", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T readBody(String body, TypeReference<T> responseType) {
        if (responseType == null) {
            return null;
        }
        if (responseType.getType() == Void.class) {
            return null;
        }
        if (body == null || body.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(body, responseType);
        } catch (JsonProcessingException e) {
            throw new Trading212Exception("Failed to parse Trading212 response body: " + e.getOriginalMessage(), e);
        } catch (IOException e) {
            throw new Trading212Exception("Failed to read Trading212 response body", e);
        }
    }

    private String writeJson(Object body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new Trading212Exception("Failed to serialize request body", e);
        }
    }
}

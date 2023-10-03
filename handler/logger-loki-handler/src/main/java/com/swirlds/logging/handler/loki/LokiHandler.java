package com.swirlds.logging.handler.loki;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.Level;
import com.swirlds.logging.api.extensions.emergency.EmergencyLogger;
import com.swirlds.logging.api.extensions.emergency.EmergencyLoggerProvider;
import com.swirlds.logging.api.extensions.event.LogEvent;
import com.swirlds.logging.api.extensions.handler.AbstractSyncedHandler;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class LokiHandler extends AbstractSyncedHandler {

    private static final EmergencyLogger EMERGENCY_LOGGER = EmergencyLoggerProvider.getEmergencyLogger();

    public LokiHandler(@NonNull final Configuration configuration) {
        super("loki", configuration);
    }

    @Override
    protected void handleEvent(@NonNull final LogEvent event) {
        final List<String> labelNames = getConfiguration().getPropertyNames()
                .filter(name -> name.startsWith("logging.handler.loki.labels."))
                .map(name -> name.substring("logging.handler.loki.labels.".length()))
                .toList();

        final Map<String, String> context = new java.util.HashMap<>();
        context.put("level", event.level().name());
        context.put("loggerName", event.loggerName());
        context.put("threadName", event.threadName());
        context.putAll(event.context());

        //TODO: add parents as list
        Optional.ofNullable(event.marker())
                .ifPresent(marker -> context.put("marker", marker.name()));

        final Map<String, String> labels = labelNames.stream()
                .filter(name -> context.containsKey(name))
                .map(name -> Map.entry(name, context.get(name)))
                .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        final JsonObject json = new JsonObject();
        final JsonArray streams = new JsonArray();
        json.add("streams", streams);
        final JsonObject stream = new JsonObject();
        final JsonObject streamLabels = new JsonObject();
        stream.add("stream", streamLabels);
        labels.forEach(streamLabels::addProperty);
        streams.add(stream);
        stream.add("stream", streamLabels);
        final JsonArray values = new JsonArray();
        stream.add("values", values);
        final JsonArray value = new JsonArray();
        values.add(value);
        value.add("" + TimeUnit.MILLISECONDS.toNanos(event.timestamp().toEpochMilli()));
        value.add(event.message().getMessage());
        final JsonObject metadata = new JsonObject();
        context.entrySet().stream()
                .filter(entry -> !labels.containsKey(entry.getKey()))
                .forEach(entry -> metadata.addProperty(entry.getKey(), entry.getValue()));
        value.add(metadata);

        try {
            post(json.toString());
        } catch (Exception e) {
            EMERGENCY_LOGGER.log(Level.ERROR, "Failed to send log event to Loki", e);
        }
    }

    private void post(@NonNull String json) throws URISyntaxException, IOException, InterruptedException {
        final BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(json);
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(getUrl())
                .header("Content-Type", "application/json")
                .POST(bodyPublisher)
                .build();
        final HttpClient httpClient = HttpClient.newBuilder().build();
        final BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        final HttpResponse<String> response = httpClient.send(request, bodyHandler);
        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new IOException(
                    "Unexpected status code: " + response.statusCode() + " response: " + response.body() + " - JSON:"
                            + json);
        }
    }

    @NonNull
    private URI getUrl() throws URISyntaxException {
        final String baseUrl = getConfiguration().getValue("logging.handler.loki.url", "http://localhost:3100");
        if (baseUrl.endsWith("/")) {
            return new URI(baseUrl + "loki/api/v1/push");
        } else {
            return new URI(baseUrl + "/loki/api/v1/push");
        }
    }
}

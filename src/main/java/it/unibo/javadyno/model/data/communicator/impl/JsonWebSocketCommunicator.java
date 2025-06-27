package it.unibo.javadyno.model.data.communicator.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import it.unibo.javadyno.controller.impl.AlertMonitor;
import it.unibo.javadyno.model.data.communicator.api.JsonScheme;
import javafx.util.Pair;

/**
 * A WebSocket‐based implementation of MCUCommunicator that uses JSON for message parsing.
 * This class implements a communication layer with an MCU WebSocket Server endpoint.
 */
public final class JsonWebSocketCommunicator extends AbstractWebSocketCommunicator<Pair<JsonScheme, Double>> {

    private static final String SERVER_URI = "192.168.100.1:8080";
    private static final String PAYLOAD_STRING = "payload";

    /**
     * Constructs a JsonWebSocketCommunicator with the server URI usually used with Aruino-like MCUs.
     * The server URI used is {@value #SERVER_URI}.
     */
    public JsonWebSocketCommunicator() {
        super(SERVER_URI);
    }

    /**
     * Constructs a JsonWebSocketCommunicator with the specified server URI.
     *
     * @param serverUri the URI of the MCU WebSocket server (e.g., {@value #SERVER_URI})
     */
    public JsonWebSocketCommunicator(final String serverUri) {
        super(serverUri);
    }

    @Override
    public void send(final String message) {
        Objects.requireNonNull(message, "Message cannot be null");
        final String jsonMessage = new JSONStringer()
            .object()
            .key(PAYLOAD_STRING)
            .value(message)
            .toString();
        super.send(jsonMessage);
    }

    @Override
    protected List<Pair<JsonScheme, Double>> parseMessage(final String message) {
        try {
            return new JSONObject(message).toMap().entrySet().stream()
                .map(entry -> new Pair<>(JsonScheme.valueOf(entry.getKey()), (Double) entry.getValue()))
                .toList();
        } catch (final JSONException e) {
            AlertMonitor.warningNotify(
                "Invalid JSON message received: " + message,
                Optional.of(e.getMessage())
            );
            throw new IllegalArgumentException("Invalid JSON message received: " + message, e);
        }
    }
}

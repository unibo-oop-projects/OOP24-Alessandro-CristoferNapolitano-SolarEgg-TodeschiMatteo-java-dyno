package it.unibo.javadyno.model.data.communicator.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import it.unibo.javadyno.controller.impl.AlertMonitor;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * This class establishes a connection to receive information messages from the MCU
 * and broadcasts incoming payloads to all registered listeners after the parsing has been
 * applied and enables sending information to the MCU through the send method.
 * Connection is NOT secure (does not use wss://).
 *
 * @param <T> the type of parsed messages that will be delivered to registered listeners
 */
public abstract class AbstractWebSocketCommunicator<T> implements MCUCommunicator<T> {

    private static final String DEFAULT_SERVER_URI = "192.168.1.1:8080";
    private static final String WEBSOCKET_PREFIX = "ws://";
    private final String mcuServerUri;
    private final Set<Consumer<T>> messageListeners = new HashSet<>();
    private InternalWSClient webSocketClient;

    /**
     * Constructs a WebSocketMCUCommunicator with the default server URI and timeout.
     * The default server URI is {@value #DEFAULT_SERVER_URI}.
     */
    public AbstractWebSocketCommunicator() {
        this(DEFAULT_SERVER_URI); // Default server URI and timeout
    }

    /**
     * Constructs a WebSocketMCUCommunicator with the specified server URI.
     *
     * @param serverUri the URI of the MCU WebSocket server (e.i. {@value #DEFAULT_SERVER_URI}).
     */
    public AbstractWebSocketCommunicator(final String serverUri) {
        this.mcuServerUri = serverUri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() {
        if (!this.isConnected()) {
            try {
                webSocketClient = new InternalWSClient(new URI(
                    WEBSOCKET_PREFIX + this.mcuServerUri
                ));
            } catch (final URISyntaxException e) {
                AlertMonitor.warningNotify(
                    "Invalid WebSocket URI: " + mcuServerUri,
                    Optional.of(e.getMessage())
                );
                //throw new IllegalArgumentException("Invalid WebSocket URI: " + mcuServerUri, e);
            }
            webSocketClient.connect();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        if (this.isConnected()) {
            webSocketClient.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        return !Objects.isNull(webSocketClient) && webSocketClient.isOpen();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final String message) {
        Objects.requireNonNull(message);
        if (webSocketClient.isOpen()) {
            webSocketClient.send(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessageListener(final Consumer<T> listener) {
        Objects.requireNonNull(listener);
        this.messageListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageListener(final Consumer<T> listener) {
        Objects.requireNonNull(listener);
        this.messageListeners.remove(listener);
    }

    /**
     * Parses the incoming message from the MCU.
     * This method should be implemented by subclasses to process the received message.
     *
     * @param message the raw message received from the MCU
     * @return a list of parsed messages where each entry consists of a pair parameter-value
     */
    protected abstract List<T> parseMessage(String message);

    private final class InternalWSClient extends WebSocketClient {

        private InternalWSClient(final URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(final ServerHandshake handshakedata) {
            AlertMonitor.infoNotify(
                "Connected to WebSocket server: " + mcuServerUri,
                Optional.empty()
            );
        }

        @Override
        public void onMessage(final String message) {
            for (final Consumer<T> listener : messageListeners) {
                for (final T parsedMessage : parseMessage(message)) {
                    listener.accept(parsedMessage);
                }
            }
        }

        @Override
        public void onClose(final int code, final String reason, final boolean remote) {
            if (code != -1) {
                AlertMonitor.infoNotify(
                    "WebSocket connection closed: " + reason,
                    Optional.of("Code: " + code)
                );
            }
        }

        @Override
        public void onError(final Exception ex) {
            AlertMonitor.warningNotify(
                "WebSocket error: " + ex.getMessage(),
                Optional.of(ex.getMessage())
            );
        }
    }
}

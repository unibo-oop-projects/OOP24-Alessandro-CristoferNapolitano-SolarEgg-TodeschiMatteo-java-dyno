package it.unibo.javadyno.model.data.communicator.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * A WebSocket‐based implementation of MCUCommunicator.
 * This class implements a communication layer with an MCU WebSocket Server endpoint
 * (e.g. ws://192.168.4.1:8080). It establishes a connection to receive
 * information messages from the MCU, broadcasts incoming payloads to all
 * registered listeners, and enables sending information to the MCU through
 * the send method. Connection is NOT secure (does not use wss://).
 */
public class WebSocketMCUCommunicator implements MCUCommunicator {

    private static final long DEFAULT_TIMEOUT = 5000;
    private static final String DEFAULT_SERVER_URI = "192.168.1.2:8080";
    private static final String WEBSOCKET_PREFIX = "ws://";
    private final String mcuServerUri;
    private final long timeOut;
    private final List<Consumer<String>> messageListeners = new ArrayList<>();
    private InternalWSClient webSocketClient;

    /**
     * Constructs a WebSocketMCUCommunicator with the default server URI and timeout.
     * The default server URI is {@value #DEFAULT_SERVER_URI} and the default timeout is {@value #DEFAULT_TIMEOUT} milliseconds.
     */
    public WebSocketMCUCommunicator() {
        this(DEFAULT_SERVER_URI, DEFAULT_TIMEOUT); // Default server URI and timeout
    }

    /**
     * Constructs a WebSocketMCUCommunicator with the specified server URI
     * and the default timeout ({@value #DEFAULT_TIMEOUT} milliseconds).
     *
     * @param serverUri the URI of the MCU WebSocket server (e.i. {@value #DEFAULT_SERVER_URI}).
     */
    public WebSocketMCUCommunicator(final String serverUri) {
        this(serverUri, DEFAULT_TIMEOUT); // Default timeout value
    }

    /**
     * Constructs a WebSocketMCUCommunicator with the specified server URI and timeout.
     *
     * @param serverUri the URI of the MCU WebSocket server (e.i. {@value #DEFAULT_SERVER_URI}). 
     * @param timeout the timeout in milliseconds for connection attempts.
     */
    public WebSocketMCUCommunicator(final String serverUri, final long timeout) {
        this.mcuServerUri = serverUri;
        this.timeOut = timeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() throws InterruptedException {
        if (!this.isConnected()) {
            try {
                webSocketClient = new InternalWSClient(new URI(
                    WEBSOCKET_PREFIX + this.mcuServerUri
                ));
            } catch (final URISyntaxException e) {
                throw new IllegalArgumentException("Invalid WebSocket URI: " + mcuServerUri, e);
            }
            webSocketClient.connectBlocking(this.timeOut, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() throws InterruptedException {
        webSocketClient.closeBlocking();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        return !Objects.isNull(webSocketClient) || webSocketClient.isOpen();
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
    public void addMessageListener(final Consumer<String> listener) {
        Objects.requireNonNull(listener);
        this.messageListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageListener(final Consumer<String> listener) {
        Objects.requireNonNull(listener);
        this.messageListeners.remove(this.messageListeners.indexOf(listener));
    }

    private final class InternalWSClient extends WebSocketClient {

        private InternalWSClient(final URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(final ServerHandshake handshakedata) {
            // Tell AlertMonitor
        }

        @Override
        public void onMessage(final String message) {
            for (final Consumer<String> listener : messageListeners) {
                listener.accept(message); // inside try-catch block?
            }
        }

        @Override
        public void onClose(final int code, final String reason, final boolean remote) {
            // Tell AlertMonitor
        }

        @Override
        public void onError(final Exception ex) {
            // Tell AlertMonitor
        }
    }
}

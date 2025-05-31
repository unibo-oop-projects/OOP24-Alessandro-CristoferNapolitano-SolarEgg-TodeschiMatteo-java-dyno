package it.unibo.javadyno.model.data.communicator.impl;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * A WebSocket‐based implementation of MCUCommunicator.
 * 
 * This class:
 *   - Can connect to an MCU WebSocket endpoint (e.g. ws://192.168.4.1:8080)
 *   - Receives info messages from the MCU
 *   - Broadcasts incoming payloads to all registered listeners
 *   - Provides a send(String) method to push info to the MCU
 */
public class WebSocketMCUCommunicator implements MCUCommunicator {

    private final URI mcuServerUri;
    private final long timeOut;
    private static final long DEFAULT_TIMEOUT = 5000;
    private final List<Consumer<String>> messageListeners = new CopyOnWriteArrayList<>();
    private InternalWSClient webSocketClient;

    /**
     * Constructs a WebSocketMCUCommunicator with the specified server URI.
     *
     * @param serverUri the URI of the MCU WebSocket server.
     */
    public WebSocketMCUCommunicator(final URI serverUri) {
        this(serverUri, DEFAULT_TIMEOUT); // Default timeout value
    }

    public WebSocketMCUCommunicator(final URI serverUri, final long timeout) {
        this.mcuServerUri = serverUri;
        this.timeOut = timeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() throws InterruptedException {
        if (!this.isConnected()) {
            webSocketClient = new InternalWSClient(this.mcuServerUri);
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'send'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessageListener(final Consumer<String> listener) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addMessageListener'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageListener(final Consumer<String> listener) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeMessageListener'");
    }

    private class InternalWSClient extends WebSocketClient {

        public InternalWSClient(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'onOpen'");
        }

        @Override
        public void onMessage(String message) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'onClose'");
        }

        @Override
        public void onError(Exception ex) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'onError'");
        }
        
    }

}

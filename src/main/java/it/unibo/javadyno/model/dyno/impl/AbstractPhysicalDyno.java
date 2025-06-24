package it.unibo.javadyno.model.dyno.impl;

import java.util.function.Consumer;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * This class implements the Dyno interface and provides common functionality
 * for physical dynamometers that communicate with an MCU (Microcontroller Unit).
 */
public abstract class AbstractPhysicalDyno implements Dyno {

    private final MCUCommunicator communicator;
    private volatile boolean active;
    private Consumer<String> messageListener;

    /**
     * Initializes the AbstractPhysicalDyno with the given MCUCommunicator.
     *
     * @param communicator the MCUCommunicator to use for communication.
     */
    public AbstractPhysicalDyno(final MCUCommunicator communicator) {
        this.communicator = new InternalMCUCommunicatorWrapper(communicator);
        this.active = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin() {
        if (!this.isActive()) {
            this.communicator.connect();
            this.active = true;
            this.messageListener = this::handleMessage;
            this.communicator.addMessageListener(this.messageListener);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end() {
        if (this.isActive()) {
            this.communicator.disconnect();
            this.active = false;
            this.communicator.removeMessageListener(this.messageListener);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        return this.active;
    }

    /**
     * Parses incoming messages from the MCUCommunicator and updates the stored data.
     * This method should be implemented by subclasses to process the message.
     *
     * @param message the JSON message received from the communicator
     */
    protected abstract void handleMessage(String message);

    /**
     * Wrapper class for MCUCommunicator to ensure that the
     * AbstractPhysicalDyno does run into an inconsistent state.
     */
    private static class InternalMCUCommunicatorWrapper implements MCUCommunicator {
        private final MCUCommunicator communicator;

        InternalMCUCommunicatorWrapper(final MCUCommunicator communicator) {
            this.communicator = communicator;
        }

        @Override
        public void connect() {
            this.communicator.connect();
        }

        @Override
        public void disconnect() {
            this.communicator.disconnect();
        }

        @Override
        public void addMessageListener(final Consumer<String> listener) {
            this.communicator.addMessageListener(listener);
        }

        @Override
        public void removeMessageListener(final Consumer<String> listener) {
            this.communicator.removeMessageListener(listener);
        }

        @Override
        public boolean isConnected() {
            return this.communicator.isConnected();
        }

        @Override
        public void send(final String message) {
            this.communicator.send(message);
        }
    }
}

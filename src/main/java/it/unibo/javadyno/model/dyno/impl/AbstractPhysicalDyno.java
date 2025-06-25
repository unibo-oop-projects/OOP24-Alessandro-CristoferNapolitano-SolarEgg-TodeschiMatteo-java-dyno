package it.unibo.javadyno.model.dyno.impl;

import java.util.Objects;
import java.util.function.Consumer;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * This class implements the Dyno interface and provides common functionality
 * for physical dynamometers that communicate with an MCU (Microcontroller Unit).
 *
 * @param <T> the type of parsed messages that will be delivered to registered listeners
 */
public abstract class AbstractPhysicalDyno<T> implements Dyno {

    private final MCUCommunicator<T> communicator;
    private volatile boolean active;
    private Consumer<T> messageListener;

    /**
     * Initializes the AbstractPhysicalDyno with the given MCUCommunicator.
     *
     * @param communicator the MCUCommunicator to use for communication.
     */
    public AbstractPhysicalDyno(final MCUCommunicator<T> communicator) {
        Objects.requireNonNull(communicator, "Communicator cannot be null");
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
     * Returns the MCUCommunicator used.
     *
     * @return the MCUCommunicator instance
     */
    public MCUCommunicator<T> getCommunicator() {
        return this.communicator;
    }

    /**
     * Parses incoming messages from the MCUCommunicator and updates the stored data.
     * This method should be implemented by subclasses to process the message.
     *
     * @param message the message received from the communicator
     */
    protected abstract void handleMessage(T message);

    /**
     * Wrapper class for MCUCommunicator to ensure that the
     * AbstractPhysicalDyno does run into an inconsistent state.
     */
    private class InternalMCUCommunicatorWrapper implements MCUCommunicator<T> {
        private final MCUCommunicator<T> communicator;

        InternalMCUCommunicatorWrapper(final MCUCommunicator<T> communicator) {
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
        public void addMessageListener(final Consumer<T> listener) {
            this.communicator.addMessageListener(listener);
        }

        @Override
        public void removeMessageListener(final Consumer<T> listener) {
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

package it.unibo.javadyno.model.dyno.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import it.unibo.javadyno.controller.impl.AlertMonitor;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * This class implements the Dyno interface and provides common functionality
 * for physical dynamometers that communicate with an MCU (Microcontroller Unit).
 *
 * @param <T> the type of parsed messages that will be delivered to registered listeners
 */
public abstract class AbstractPhysicalDyno<T> implements Dyno, Runnable {

    private static final int DEFAULT_POLLING = 100;
    private final MCUCommunicator<T> communicator;
    private final int polling;
    private volatile boolean active;
    private Consumer<T> messageListener;

    /**
     * Initializes the AbstractPhysicalDyno with the given MCUCommunicator.
     *
     * @param communicator the MCUCommunicator to use for communication.
     */
    public AbstractPhysicalDyno(final MCUCommunicator<T> communicator) {
        this(communicator, DEFAULT_POLLING);
    }

    /**
     * Initializes the AbstractPhysicalDyno with the given MCUCommunicator and polling interval.
     *
     * @param communicator the MCUCommunicator to use for communication.
     * @param polling      the polling interval in milliseconds.
     */
    public AbstractPhysicalDyno(final MCUCommunicator<T> communicator, final int polling) {
        this.communicator = new WrapperCommunicator(Objects.requireNonNull(communicator, "Communicator cannot be null"));
        if (polling <= 0) {
            AlertMonitor.warningNotify(
                "Polling interval must be greater than zero",
                Optional.empty()
            );
            throw new IllegalArgumentException("Polling interval must be greater than zero");
        }
        this.polling = polling;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin() {
        if (!this.isActive()) {
            this.communicator.connect();
            this.messageListener = this::listenForError;
            this.communicator.addMessageListener(this.messageListener);
            this.active = true;
            Thread.ofVirtual()
                .name(getThreadName())
                .start(this);
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
     * {@inheritDoc}
     */
    @Override
    public void run() {
        while (this.isActive()) {
            final String outgoingMessage = this.getOutgoingMessage();
            if (Objects.nonNull(outgoingMessage) && !outgoingMessage.isBlank()) {
                this.communicator.send(outgoingMessage);
            }

            try {
                Thread.sleep(this.polling);
            } catch (final InterruptedException e) {
                AlertMonitor.errorNotify(
                    "Dyno thread interrupted",
                    Optional.of("Error: " + e.getMessage())
                );
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Parses incoming messages from the MCUCommunicator and updates the stored data.
     * This method should be implemented by subclasses to process the message.
     *
     * @param message the message received from the communicator (can be null)
     */
    protected abstract void handleMessage(T message);

    /**
     * Returns the message to be sent to the MCU.
     * This method should be implemented by subclasses to provide a specific outgoing message.
     *
     * @return the outgoing message as a String
     */
    protected abstract String getOutgoingMessage();

    /**
     * Returns the name of the thread used for running the dyno.
     * This method should be implemented by subclasses to provide a specific thread name.
     *
     * @return the name of the thread
     */
    protected abstract String getThreadName();

    private void listenForError(final T message) {
        if (Objects.isNull(message)) {
            this.end();
            return;
        }
        handleMessage(message);
    }

    private class WrapperCommunicator implements MCUCommunicator<T> {
        private final MCUCommunicator<T> wrapped;

        WrapperCommunicator(final MCUCommunicator<T> wrapped) {
            this.wrapped = Objects.requireNonNull(wrapped, "Wrapped communicator cannot be null");
        }

        @Override
        public void connect() {
            wrapped.connect();
        }

        @Override
        public void disconnect() {
            wrapped.disconnect();
        }

        @Override
        public void send(final String message) {
            wrapped.send(message);
        }

        @Override
        public void addMessageListener(final Consumer<T> listener) {
            wrapped.addMessageListener(listener);
        }

        @Override
        public void removeMessageListener(final Consumer<T> listener) {
            wrapped.removeMessageListener(listener);
        }

        @Override
        public boolean isConnected() {
            return wrapped.isConnected();
        }
    }

}

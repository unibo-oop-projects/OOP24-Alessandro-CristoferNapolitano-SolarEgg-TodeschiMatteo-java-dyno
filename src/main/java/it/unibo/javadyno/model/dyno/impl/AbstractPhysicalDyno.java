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
        Objects.requireNonNull(communicator, "Communicator cannot be null");
        if (polling <= 0) {
            throw new IllegalArgumentException("Polling interval must be greater than zero");
        }
        this.communicator = communicator;
        this.polling = polling;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin() {
        if (!this.isActive()) {
            this.communicator.connect();
            this.messageListener = this::handleMessage;
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
     * Returns the MCUCommunicator used.
     *
     * @return the MCUCommunicator instance
     */
    public MCUCommunicator<T> getCommunicator() {
        return this.communicator;
    }

    @Override
    public void run() {
        while (this.isActive()) {
            final String outgoingMessage = this.getOutgoingMessage();
            Objects.requireNonNull(outgoingMessage, "Outgoing message cannot be null");
            if (!outgoingMessage.isBlank()) {
                this.communicator.send(outgoingMessage);
            }

            try {
                Thread.sleep(this.polling);
            } catch (final InterruptedException e) {
                // Tell alert monitor
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Parses incoming messages from the MCUCommunicator and updates the stored data.
     * This method should be implemented by subclasses to process the message.
     *
     * @param message the message received from the communicator
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


}

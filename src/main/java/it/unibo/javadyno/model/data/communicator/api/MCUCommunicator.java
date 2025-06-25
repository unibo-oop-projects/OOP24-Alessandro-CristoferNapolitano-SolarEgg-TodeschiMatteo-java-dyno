package it.unibo.javadyno.model.data.communicator.api;

import java.util.function.Consumer;

/**
 * Interface for communicating with a microcontroller unit (MCU).
 * Provides methods to connect, disconnect, send messages, and manage message listeners.
 * Abstracts the communication layer for different types of MCUs and communication protocols.
 */
public interface MCUCommunicator {

    /**
     * Establishes (or re‐establishes) the connection to the MCU synchronically.
     * If already connected, this can be a no‐op.
     */
    void connect();

    /**
     * Disconnects from the MCU in a synchronous way.
     * If not connected, this can be a no‐op.
     */
    void disconnect();

    /**
     * Checks if the communicator is currently connected to the MCU.
     *
     * @return true if connected, false otherwise.
     */
    boolean isConnected();

    /**
     * Sends a formatted message to the MCU.
     * The message format and protocol depend on the specific implementation.
     *
     * @param message the message to send.
     */
    void send(String message);

    /**
     * Register a listener that will be invoked whenever a message arrives from the MCU.
     *
     * @param listener a Consumer that handles incoming messages.
     */
    void addMessageListener(Consumer<String> listener);

    /**
     * Removes a previously registered message listener.
     *
     * @param listener the Consumer to remove from the listeners list.
     */
    void removeMessageListener(Consumer<String> listener);

}

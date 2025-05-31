package it.unibo.javadyno.model.data.communicator.impl;

import java.util.function.Consumer;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;

/**
 * Implementation of the MCUCommunicator interface.
 * This class provides methods to connect, disconnect, send messages,
 * and manage message listeners for communication with a microcontroller unit (MCU).
 */
public class MCUCommunicatorImpl implements MCUCommunicator {

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addMessageListener'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addMessageListener'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addMessageListener'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addMessageListener'");
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

}

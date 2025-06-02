package it.unibo.javadyno.model.data.communicator.impl;

import java.util.function.Consumer;

import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;

/**
 * Implementation of MCUCommunicator for serial communication with a microcontroller unit (MCU).
 * It is designed to work over USB with ELM327 compatible MCUs.
 */
public class SerialMCUCommunicator implements MCUCommunicator {

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connect'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disconnect'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isConnected'");
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
}

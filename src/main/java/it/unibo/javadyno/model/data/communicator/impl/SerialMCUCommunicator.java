package it.unibo.javadyno.model.data.communicator.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;

/**
 * Implementation of MCUCommunicator for serial communication with a microcontroller unit (MCU).
 * It is designed to work over USB with ELM327 compatible MCUs.
 */
public class SerialMCUCommunicator implements MCUCommunicator {

    private static final int INVALID_VENDOR_ID = -1;
    private static final int NEW_WRITE_TIMEOUT = 0;
    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int DEFAULT_BAUD_RATE = 38_400;
    private final String suppliedPort;
    private final int timeOut;
    private final int baudRate;
    private final List<Consumer<String>> messageListeners = new ArrayList<>();
    private SerialPort commPort;

    /**
     * Constructs a SerialMCUCommunicator auto-detecting the
     * serial port to use.
     */
    public SerialMCUCommunicator() {
        this(null);
    }

    /**
     * Constructs a SerialMCUCommunicator with the specified serial port.
     *
     * @param suppliedPort the name of the serial port to be used for communication (as a String)
     */
    public SerialMCUCommunicator(final String suppliedPort) {
        this(DEFAULT_TIMEOUT, DEFAULT_BAUD_RATE, suppliedPort);
    }

    /**
     * Constructs a SerialMCUCommunicator with the specified timeout and baud rate.
     *
     * @param timeOut the timeout in milliseconds for connection attempts
     * @param baudRate the baud rate for serial communication 
     */
    public SerialMCUCommunicator(final int timeOut, final int baudRate) {
        this(timeOut, baudRate, null);
    }

    /**
     * Constructs a SerialMCUCommunicator with the specified timeout, baud rate, and serial port.
     *
     * @param timeOut the timeout in milliseconds for connection attempts
     * @param baudRate the baud rate for serial communication
     * @param suppliedPort the name of the serial port to be used for communication (as a String)
     */
    public SerialMCUCommunicator(final int timeOut, final int baudRate, final String suppliedPort) {
        this.suppliedPort = suppliedPort;
        this.timeOut = timeOut;
        this.baudRate = baudRate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() throws InterruptedException {
        if (!this.isConnected()) {
            if (Objects.isNull(this.suppliedPort)) {
                final Set<SerialPort> ports = Set.of(SerialPort.getCommPorts());
                if (ports.isEmpty()) {
                    throw new IllegalStateException("No serial ports available for connection.");
                    // tell alert monitor
                }
                for (final SerialPort serialPort : ports) {
                    if (serialPort.getVendorID() != INVALID_VENDOR_ID) {
                        this.commPort = serialPort;
                        break;
                    }
                }
                throw new IllegalStateException("No valid serial ports.");

            } else {
                this.commPort = SerialPort.getCommPort(this.suppliedPort);

            }

            this.commPort.setBaudRate(this.baudRate);
            this.commPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, this.timeOut, NEW_WRITE_TIMEOUT);
            this.commPort.addDataListener(new DataListener());
            this.commPort.addDataListener(new DisconnectListener());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() throws InterruptedException {
        if (this.isConnected()) {
            if (!this.commPort.closePort()) {
                throw new IllegalStateException("Failed to close the serial port: " + this.commPort.getSystemPortName());
                // tell alert monitor
            }
            this.commPort = null;
            this.messageListeners.clear();
            this.commPort.removeDataListener();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        return !Objects.isNull(this.commPort) && this.commPort.isOpen();
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
        Objects.requireNonNull(listener);
        this.messageListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageListener(final Consumer<String> listener) {
        Objects.requireNonNull(listener);
        if (this.messageListeners.contains(listener)) {
            this.messageListeners.remove(this.messageListeners.indexOf(listener));
        }
    }

    /**
     * Internal listener for serial port data events.
     */
    private final class DataListener implements SerialPortDataListener {

        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
        }

        @Override
        public void serialEvent(final SerialPortEvent event) {
            if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                final byte[] readBuffer = new byte[commPort.bytesAvailable()];
                commPort.readBytes(readBuffer, readBuffer.length);
                final String message = new String(readBuffer);
                for (final Consumer<String> listener : SerialMCUCommunicator.this.messageListeners) {
                    listener.accept(message);
                }
            }
        }
    }

    /**
     * Internal listener for serial port disconnection events as they cannot be detected otherwise.
     */
    private final class DisconnectListener implements SerialPortDataListener {

        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
        }

        @Override
        public void serialEvent(final SerialPortEvent event) {
            try {
                disconnect();
            } catch (final InterruptedException e) {
                // tell alter monitor
                throw new IllegalStateException("Interrupted while disconnecting from serial port", e);
            }
        }
    }
}

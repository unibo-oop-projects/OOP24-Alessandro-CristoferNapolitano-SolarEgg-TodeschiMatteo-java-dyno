package it.unibo.javadyno.model.data.communicator.impl;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import com.fazecast.jSerialComm.SerialPort;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;

/**
 * Implementation of MCUCommunicator for serial communication with a microcontroller unit (MCU).
 * It is designed to work over USB with ELM327 compatible MCUs.
 */
public class SerialMCUCommunicator implements MCUCommunicator {

    private static final int INVALID_VENDOR_ID = -1;
    private static final int NEW_WRITE_TIMEOUT = 0;
    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int DEFAULT_BAUD_RATE = 115200;
    private SerialPort commPort;
    private final String suppliedPort;
    private final int timeOut;
    private final int baudRate;

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
     * @param commPort the name of the serial port to be used for communication (as a String)
     */
    public SerialMCUCommunicator(final String suppliedPort) {
        this(DEFAULT_TIMEOUT, DEFAULT_BAUD_RATE, suppliedPort);
    }

    public SerialMCUCommunicator(final int timeOut, final int baudRate) {
        this(timeOut, baudRate, null);
    }

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
                for (SerialPort serialPort : ports) {
                    if (serialPort.getVendorID() != INVALID_VENDOR_ID) {
                        this.commPort = serialPort;
                        this.commPort.setBaudRate(this.baudRate);
                        this.commPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, this.timeOut, NEW_WRITE_TIMEOUT);
                        break;
                    }
                }
            } else {
                this.commPort = SerialPort.getCommPort(this.suppliedPort);
            }
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

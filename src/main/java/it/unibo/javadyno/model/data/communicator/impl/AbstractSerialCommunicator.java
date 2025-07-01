package it.unibo.javadyno.model.data.communicator.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

import it.unibo.javadyno.controller.impl.AlertMonitor;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;

/**
 * Abstract class for serial communication with a MCU .
 * It provides methods to connect, disconnect, send messages, and manage message listeners.
 * This class is designed to be extended by specific implementations for different MCU types.
 *
 * @param <T> the type of parsed messages that will be delivered to registered listeners
 */
public abstract class AbstractSerialCommunicator<T> implements MCUCommunicator<T> {

    private static final int INVALID_VENDOR_ID = -1;
    private static final int DEFAULT_BAUD_RATE = 9200;
    private final String suppliedPort;
    private final int baudRate;
    private SerialPort commPort;
    private final Set<Consumer<T>> messageListeners = new HashSet<>();

    /**
     * Constructs a SerialCommunicator auto-detecting the
     * serial port to use.
     */
    public AbstractSerialCommunicator() {
        this(null);
    }

    /**
     * Constructs a SerialCommunicator with the specified serial port.
     *
     * @param suppliedPort the name of the serial port to be used for communication (as a String)
     */
    public AbstractSerialCommunicator(final String suppliedPort) {
        this(DEFAULT_BAUD_RATE, suppliedPort);
    }

    /**
     * Constructs a SerialCommunicator with the specified timeout and baud rate.
     *
     * @param baudRate the baud rate for serial communication 
     */
    public AbstractSerialCommunicator(final int baudRate) {
        this(baudRate, null);
    }

    /**
     * Constructs a SerialCommunicator with the specified timeout, baud rate, and serial port.
     *
     * @param baudRate the baud rate for serial communication
     * @param suppliedPort the name of the serial port to be used for communication (as a String)
     */
    public AbstractSerialCommunicator(final int baudRate, final String suppliedPort) {
        this.suppliedPort = suppliedPort;
        this.baudRate = baudRate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() {
        if (!this.isConnected()) {
            if (Objects.isNull(this.suppliedPort)) {
                final Set<SerialPort> ports = Set.of(SerialPort.getCommPorts());
                if (ports.isEmpty()) {
                    AlertMonitor.warningNotify(
                        "No serial ports available for connection",
                        Optional.of("Verify USB connection or drivers.")
                    );
                }
                for (final SerialPort serialPort : ports) {
                    if (serialPort.getVendorID() != INVALID_VENDOR_ID) {
                        this.commPort = serialPort;
                        break;
                    }
                }
                AlertMonitor.warningNotify(
                    "No valid serial ports found.", 
                    Optional.of("Check if the Dyno is connected or if the drivers are installed.")
                );
                return;
            } else {
                this.commPort = SerialPort.getCommPort(this.suppliedPort);

            }
            this.commPort.setBaudRate(this.baudRate);
            this.commPort.openPort();
            try {
                this.setupChip(this.commPort);
            } catch (final InterruptedException e) {
                AlertMonitor.warningNotify(
                    "Failed to setup the chip on port: " + this.commPort.getSystemPortName(),
                    Optional.empty()
                );
                //throw new IllegalStateException("Failed to setup the chip on port: " + this.commPort.getSystemPortName(), e);
            }
            this.commPort.addDataListener(new DataListener());
            this.commPort.addDataListener(new DisconnectListener());
            AlertMonitor.infoNotify(
                "Connected to serial port: " + this.commPort.getSystemPortName(),
                Optional.empty()
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        if (this.isConnected()) {
            if (!this.commPort.closePort()) {
                AlertMonitor.warningNotify(
                    "Failed to close the serial port: " + this.commPort.getSystemPortName(),
                    Optional.empty()
                    );
                //throw new IllegalStateException("Failed to close the serial port: " + this.commPort.getSystemPortName());
            }
            this.messageListeners.clear();
            this.commPort.removeDataListener();
            AlertMonitor.infoNotify(
                "Disconnected from serial port: " + this.commPort.getSystemPortName(),
                Optional.empty()
            );
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
        if (this.isConnected()) {
            final byte[] bytes = (message + getSentDataDelimiter()).getBytes(StandardCharsets.UTF_8);
            this.commPort.writeBytes(bytes, bytes.length);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessageListener(final Consumer<T> listener) {
        Objects.requireNonNull(listener);
        this.messageListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessageListener(final Consumer<T> listener) {
        Objects.requireNonNull(listener);
        this.messageListeners.remove(listener);
    }

    /**
     * Returns the serial port used for communication.
     *
     * @return the SerialPort instance used for communication
     */
    protected SerialPort getCommPort() {
        return this.commPort;
    }

    /**
     * Returns the set of message listeners.
     *
     * @return a Set of Consumer that are registered to receive messages
     */
    protected Set<Consumer<T>> getMessageListeners() {
        return this.messageListeners;
    }

    /**
     * Sets up the chip on the serial port.
     * This method should be implemented by subclasses to perform specific setup actions.
     *
     * @param port the SerialPort instance to set up
     * @throws InterruptedException if the setup process is interrupted
     */
    protected abstract void setupChip(SerialPort port) throws InterruptedException;

    /**
     * Returns the delimiter used to send data to the MCU.
     * This method should be implemented by subclasses to specify the delimiter
     *
     * @return the delimiter as a String
     */
    protected abstract String getSentDataDelimiter();

    /**
     * Returns the delimiter used to truncate received data from the MCU.
     * This method should be implemented by subclasses to specify the delimiter
     *
     * @return the delimiter as a String
     */
    protected abstract String getRecievedDataDelimiter();

    /**
     * Parses the incoming message from the MCU.
     * This method should be implemented by subclasses to process the received message.
     */
    protected abstract void parseMessage();

    /**
     * Internal listener for serial port data events.
     */
    private final class DataListener implements SerialPortMessageListener {

        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
        }

        @Override
        public byte[] getMessageDelimiter() {
            return getRecievedDataDelimiter().getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public boolean delimiterIndicatesEndOfMessage() {
            return true;
        }

        @Override
        public void serialEvent(final SerialPortEvent event) {
            if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                parseMessage();
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
            if (event.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED) {
                disconnect();
                AlertMonitor.warningNotify(
                    "Serial port disconnected: " + commPort.getSystemPortName(),
                    Optional.empty()
                );
                for (final Consumer<T> listener : messageListeners) {
                    listener.accept(null);
                }
            }
        }
    }
}

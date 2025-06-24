package it.unibo.javadyno.model.data.communicator.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;

public abstract class SerialCommunicator implements MCUCommunicator {

    private static final int INVALID_VENDOR_ID = -1;
    private static final int DEFAULT_BAUD_RATE = 9200;
    private final String suppliedPort;
    private final int baudRate;
    private SerialPort commPort;
    private final Set<Consumer<String>> messageListeners = new HashSet<>();

    /**
     * Constructs a SerialCommunicator auto-detecting the
     * serial port to use.
     */
    public SerialCommunicator() {
        this(null);
    }

    /**
     * Constructs a SerialCommunicator with the specified serial port.
     *
     * @param suppliedPort the name of the serial port to be used for communication (as a String)
     */
    public SerialCommunicator(final String suppliedPort) {
        this(DEFAULT_BAUD_RATE, suppliedPort);
    }

    /**
     * Constructs a SerialCommunicator with the specified timeout and baud rate.
     *
     * @param baudRate the baud rate for serial communication 
     */
    public SerialCommunicator(final int baudRate) {
        this(baudRate, null);
    }

    /**
     * Constructs a SerialCommunicator with the specified timeout, baud rate, and serial port.
     *
     * @param baudRate the baud rate for serial communication
     * @param suppliedPort the name of the serial port to be used for communication (as a String)
     */
    public SerialCommunicator(final int baudRate, final String suppliedPort) {
        this.suppliedPort = suppliedPort;
        this.baudRate = baudRate;
    }


    @Override
    public void connect() {
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
            this.commPort.openPort();
            try {
                this.setupChip(this.commPort);
            } catch (final InterruptedException e) {
                throw new IllegalStateException("Failed to setup the chip on port: " + this.commPort.getSystemPortName(), e);
                // tell alert monitor
            }
            this.commPort.addDataListener(new DataListener());
            this.commPort.addDataListener(new DisconnectListener());
        }
    }


    @Override
    public void disconnect() {
        if (this.isConnected()) {
            if (!this.commPort.closePort()) {
                throw new IllegalStateException("Failed to close the serial port: " + this.commPort.getSystemPortName());
                // tell alert monitor
            }
            this.messageListeners.clear();
            this.commPort.removeDataListener();
            this.commPort = null;
        }
    }

    @Override
    public boolean isConnected() {
        return !Objects.isNull(this.commPort) && this.commPort.isOpen();
    }


    @Override
    public void send(final String message) {
        if (this.isConnected()) {
            final byte[] bytes = (message + getSentDataDelimiter()).getBytes(StandardCharsets.UTF_8);
            this.commPort.writeBytes(bytes, bytes.length);
        }
    }


    @Override
    public void addMessageListener(final Consumer<String> listener) {
        Objects.requireNonNull(listener);
        this.messageListeners.add(listener);
    }


    @Override
    public void removeMessageListener(final Consumer<String> listener) {
        Objects.requireNonNull(listener);
        if (this.messageListeners.contains(listener)) {
            this.messageListeners.remove(listener);
        }
    }

    protected SerialPort getCommPort() {
        return this.commPort;
    }

    protected abstract void setupChip(final SerialPort port) throws InterruptedException;

    protected abstract String getSentDataDelimiter();

    protected abstract String getRecievedDataDelimiter();

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
                final byte[] readBuffer = new byte[commPort.bytesAvailable()];
                commPort.readBytes(readBuffer, readBuffer.length);
                final String message = new String(readBuffer).replace(getRecievedDataDelimiter(), "").trim();
                for (final Consumer<String> listener : messageListeners) {
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
            if (event.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED) {
                disconnect();
            }
        }
    }
}

package it.unibo.javadyno.model.data.communicator.impl;

import java.nio.charset.Charset;
import java.util.function.Consumer;
import com.fazecast.jSerialComm.SerialPort;

/**
 * Implementation of MCUCommunicator for serial communication with a microcontroller unit (MCU).
 * It is designed to work over USB with ELM327 compatible MCUs.
 */
public final class ELM327Communicator extends AbstractSerialCommunicator {

    private static final int ELM327_BAUD_RATE = 13_200;
    private static final String ENCODING = "UTF-8";
    private static final String SENT_DATA_DELIMITER = "\r";
    private static final String RECIEVED_DATA_DELIMITER = ">";
    private static final String SOFT_RESET_COMMAND = "AT WS";
    private static final String DISABLE_ECHO_COMMAND = "AT E0";
    private static final int DELAY = 100;

    /**
     * Constructs an ELM327Communicator with the specific baud rate.
     */
    public ELM327Communicator() {
        super(ELM327_BAUD_RATE);
    }

    @Override
    protected void setupChip(final SerialPort port) throws InterruptedException {
        if (port.isOpen()) {
            this.send(SOFT_RESET_COMMAND);
            Thread.sleep(DELAY);
            this.send(DISABLE_ECHO_COMMAND);
            Thread.sleep(DELAY);
        } else {
            throw new IllegalStateException("Serial port is not open: " + port.getSystemPortName());
            // tell alert monitor
        }
    }

    @Override
    protected String getSentDataDelimiter() {
        return SENT_DATA_DELIMITER;
    }

    @Override
    protected String getRecievedDataDelimiter() {
        return RECIEVED_DATA_DELIMITER;
    }

    @Override
    protected void parseMessage() {
        final SerialPort commPort = super.getCommPort();
        final byte[] readBuffer = new byte[commPort.bytesAvailable()];
        commPort.readBytes(readBuffer, readBuffer.length);
        final String message = new String(readBuffer, Charset.forName(ENCODING)).replace(getRecievedDataDelimiter(), "").trim();
        for (final Consumer<String> listener : super.getMessageListeners()) {
            listener.accept(message);
        }
    }
}

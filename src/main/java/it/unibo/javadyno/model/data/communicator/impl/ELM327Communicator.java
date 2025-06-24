package it.unibo.javadyno.model.data.communicator.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;

/**
 * Implementation of MCUCommunicator for serial communication with a microcontroller unit (MCU).
 * It is designed to work over USB with ELM327 compatible MCUs.
 */
public class ELM327Communicator extends SerialCommunicator {

    private static final int ELM327_BAUD_RATE = 13_200;
    private static final String SENT_DATA_DELIMITER = "\r";
    private static final String RECIEVED_DATA_DELIMITER = ">";
    private static final String SOFT_RESET_COMMAND = "AT WS";
    private static final String DISABLE_ECHO_COMMAND = "AT E0";
    private static final int DELAY = 100;

    public ELM327Communicator() {
        super(ELM327_BAUD_RATE);
    }

    private void setupChip(final SerialPort port) throws InterruptedException {
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


}

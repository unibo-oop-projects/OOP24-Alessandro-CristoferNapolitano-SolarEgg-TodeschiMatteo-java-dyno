package it.unibo.javadyno.model.data.communicator.impl;

import com.fazecast.jSerialComm.SerialPort;
import org.junit.jupiter.api.*;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

class SerialMCUCommunicatorTest {
    private static final String TEST_PORT = "COM1"; // Use a dummy port for testing
    private SerialMCUCommunicator communicatorWithPort;
    private SerialMCUCommunicator communicatorAuto;

    @BeforeEach
    void setUp() {
        communicatorWithPort = new SerialMCUCommunicator(TEST_PORT);
        communicatorAuto = new SerialMCUCommunicator();
    }

    @Test
    void testPortInfo() {
        try {
            communicatorAuto.connect();
        } catch (InterruptedException e) {
            fail("Connection interrupted: " + e.getMessage());
        }
    }

    @Test
    void testConstructorWithPort() {
        assertNotNull(communicatorWithPort);
    }

    @Test
    void testConstructorAutoDetect() {
        assertNotNull(communicatorAuto);
    }

    @Test
    void testIsConnectedInitiallyFalse() {
        assertFalse(communicatorWithPort.isConnected());
        assertFalse(communicatorAuto.isConnected());
    }

    @Test
    void testSendThrowsUnsupportedOperation() {
        assertThrows(UnsupportedOperationException.class, () -> communicatorWithPort.send("test"));
    }

    @Test
    void testAddMessageListenerThrowsUnsupportedOperation() {
        assertThrows(UnsupportedOperationException.class, () -> communicatorWithPort.addMessageListener(s -> {}));
    }

    @Test
    void testRemoveMessageListenerThrowsUnsupportedOperation() {
        assertThrows(UnsupportedOperationException.class, () -> communicatorWithPort.removeMessageListener(s -> {}));
    }

    @Test
    void testDisconnectWhenNotConnected() {
        assertDoesNotThrow(() -> communicatorWithPort.disconnect());
    }

    @Test
    void testConnectWithInvalidPort() {
        // This test expects an exception or no connection, depending on environment
        assertDoesNotThrow(() -> communicatorWithPort.connect());
    }
}

package it.unibo.javadyno.model.data.communicator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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
        } catch (final InterruptedException | IllegalStateException e) {
            if (e instanceof IllegalStateException) {
                assertFalse(communicatorAuto.isConnected()); // This is expected if no port is available
                return;
            }
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
    void testDisconnectWhenNotConnected() {
        assertDoesNotThrow(communicatorWithPort::disconnect);
    }

    @Test
    void testConnectWithInvalidPort() {
        // This test expects an exception or no connection, depending on environment
        assertDoesNotThrow(communicatorWithPort::connect);
    }
}

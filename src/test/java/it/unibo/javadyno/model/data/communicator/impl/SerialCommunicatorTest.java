package it.unibo.javadyno.model.data.communicator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.AfterEach;

class SerialCommunicatorTest {
    private ELM327Communicator communicatorAuto;

    @BeforeEach
    void setUp() {
        communicatorAuto = new ELM327Communicator();
    }

    @AfterEach
    void tearDown() {
        if (communicatorAuto.isConnected()) {
            communicatorAuto.disconnect();
        }
        communicatorAuto = null;
    }

    @Test
    void testConstructorAutoDetect() {
        assertNotNull(communicatorAuto);
    }

    @Test
    void testIsConnectedInitiallyFalse() {
        assertFalse(communicatorAuto.isConnected());
    }

    @Test
    void testConnection() {
        assertThrows(
            NullPointerException.class,
            communicatorAuto::connect,
            "Alert monitor does throw a NullPointerException when the controller is not initialized.");
        assertFalse(communicatorAuto.isConnected());

    }

    @Test
    void testMessageListener() {
        communicatorAuto.addMessageListener(message -> {
            assertNotNull(message);
            assertFalse(message.isEmpty());
        });
    }

    @Test
    void testSendMessage() {
        assertDoesNotThrow(() -> communicatorAuto.send("AT Z"));
    }

    @Test
    void testDisconnect() {
        assertDoesNotThrow(communicatorAuto::disconnect);
    }
}

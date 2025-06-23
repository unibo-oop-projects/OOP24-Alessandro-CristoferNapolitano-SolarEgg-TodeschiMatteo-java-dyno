package it.unibo.javadyno.model.dyno.obd2.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;

class OBD2DynoTest {
    private static final int POLLING_INTERVAL = 10;
    private static final int EXPECTED_RPM = 1674;
    private static final int EXPECTED_SPEED = 60;
    private static final int RAW_TEMP_VALUE = 0x5A;
    private static final int TEMP_OFFSET = 40;
    private static final double EXPECTED_TEMP = RAW_TEMP_VALUE - TEMP_OFFSET;
    private static final int THREAD_SLEEP_MS = 50;

    private OBD2Dyno dyno;
    private TestMCUCommunicator testCommunicator;

    @BeforeEach
    void setUp() {
        testCommunicator = new TestMCUCommunicator();
        dyno = new OBD2Dyno(testCommunicator, POLLING_INTERVAL);
    }

    @Test
    void testInitialState() {
        final RawData data = dyno.getRawData();
        assertTrue(data.engineRPM().isEmpty(), "Engine RPM should be empty initially");
        assertTrue(data.vehicleSpeed().isEmpty(), "Vehicle speed should be empty initially");
        assertTrue(data.engineTemperature().isEmpty(), "Engine temperature should be empty initially");
        assertEquals(DataSource.OBD2, dyno.getDynoType());
        assertFalse(dyno.isActive(), "Dyno should not be active initially");
    }

    @Test
    void testBeginAndEnd() throws InterruptedException {
        assertFalse(dyno.isActive());
        dyno.begin();
        assertTrue(dyno.isActive());
        // Allow the run loop to start
        Thread.sleep(THREAD_SLEEP_MS);
        dyno.end();
        assertFalse(dyno.isActive());
    }

    @Test
    void testMessageHandler() throws InterruptedException {
        dyno.begin();
        Thread.sleep(THREAD_SLEEP_MS);

        testCommunicator.simulateIncomingMessage("410C1A2B");
        assertEquals(Optional.of(EXPECTED_RPM), dyno.getRawData().engineRPM());

        testCommunicator.simulateIncomingMessage("410D3C");
        assertEquals(Optional.of(EXPECTED_SPEED), dyno.getRawData().vehicleSpeed());

        testCommunicator.simulateIncomingMessage("41055A");
        assertEquals(Optional.of(EXPECTED_TEMP), dyno.getRawData().engineTemperature());
        dyno.end();
    }

    private static final class TestMCUCommunicator implements MCUCommunicator {
        private Consumer<String> messageListener;
        private Runnable sendHook;
        private boolean connected;

        @Override
        public void connect() throws InterruptedException {
            this.connected = true;
        }

        @Override
        public void disconnect() throws InterruptedException {
            this.connected = false;
        }

        @Override
        public boolean isConnected() {
            return this.connected;
        }

        @Override
        public void send(final String message) {
            if (sendHook != null) {
                sendHook.run();
            }
        }

        @Override
        public void addMessageListener(final Consumer<String> listener) {
            this.messageListener = listener;
        }

        @Override
        public void removeMessageListener(final Consumer<String> listener) {
            this.messageListener = null;
        }

        public void simulateIncomingMessage(final String message) {
            if (messageListener != null) {
                messageListener.accept(message);
            }
        }
    }
}

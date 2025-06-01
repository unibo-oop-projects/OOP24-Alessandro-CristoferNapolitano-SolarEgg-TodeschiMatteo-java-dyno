package it.unibo.javadyno.model.dyno.obd2.impl;

import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.api.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OBD2DynoTest {
    private OBD2Dyno dyno;

    @BeforeEach
    void setUp() {
        dyno = new OBD2Dyno();
    }

    @Test
    void testInitialState() {
        RawData data = dyno.getRawData();
        assertTrue(data.engineRPM().isEmpty(), "Engine RPM should be empty initially");
        assertTrue(data.vehicleSpeed().isEmpty(), "Vehicle speed should be empty initially");
        assertTrue(data.engineTemperature().isEmpty(), "Engine temperature should be empty initially");
        assertEquals(DataSource.OBD2, dyno.getDynoType());
        assertFalse(dyno.isActive(), "Dyno should not be active initially");
    }

    @Test
    void testBeginAndEnd() {
        System.out.println("Starting dyno...");
        dyno.begin(); // WILL timeout if no WebSocket server is running
        System.out.println("Dyno started: " + dyno.isActive());
        assertTrue(dyno.isActive(), "Dyno should be active after begin()");
        System.out.println("Runnign");
        dyno.end();
        assertFalse(dyno.isActive(), "Dyno should not be active after end()");
    }

    @Test
    void testMessageHandlerUpdatesFields() {
        String json = "{" +
                "\"engineRPM\": 1234, " +
                "\"vehicleSpeed\": 56, " +
                "\"engineTemperature\": 78.9" +
                "}";
        // Use reflection to call private method for test purposes
        // https://stackoverflow.com/questions/15595765/invoking-a-private-method-via-jmockit-to-test-result/15612040#15612040
        try {
            var method = OBD2Dyno.class.getDeclaredMethod("messageHandler", String.class);
            method.setAccessible(true);
            method.invoke(dyno, json);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
        RawData data = dyno.getRawData();
        assertEquals(Optional.of(1234), data.engineRPM());
        assertEquals(Optional.of(56), data.vehicleSpeed());
        assertEquals(Optional.of(78.9), data.engineTemperature());
    }
}

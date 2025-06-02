package it.unibo.javadyno.model.dyno.obd2.impl;

import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.api.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class OBD2DynoTest {
    private static final int TEST_RPM = 1234;
    private static final int TEST_SPEED = 56;
    private static final double TEST_TEMPERATURE = 78.9;
    private OBD2Dyno dyno;

    @BeforeEach
    void setUp() {
        dyno = new OBD2Dyno();
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
    void testBeginAndEnd() {
        dyno.begin(); // WILL timeout if no WebSocket server is running
        assertTrue(dyno.isActive(), "Dyno should be active after begin()");
        dyno.end();
        assertFalse(dyno.isActive(), "Dyno should not be active after end()");
    }

    @Test
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    void testMessageHandlerUpdatesFields() {
        final String json = "{"
                + "\"engineRPM\": " + TEST_RPM + ", "
                + "\"vehicleSpeed\": " + TEST_SPEED + ", "
                + "\"engineTemperature\": " + TEST_TEMPERATURE
                + "}";
        // Use reflection to call private method for test purposes
        // https://stackoverflow.com/questions/15595765/invoking-a-private-method-via-jmockit-to-test-result/15612040#15612040
        try {
            final var method = OBD2Dyno.class.getDeclaredMethod("messageHandler", String.class);
            method.setAccessible(true);
            method.invoke(dyno, json);
        } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Reflection failed: " + e.getMessage());
        }
        final RawData data = dyno.getRawData();
        assertEquals(Optional.of(TEST_RPM), data.engineRPM());
        assertEquals(Optional.of(TEST_SPEED), data.vehicleSpeed());
        assertEquals(Optional.of(TEST_TEMPERATURE), data.engineTemperature());
    }
}

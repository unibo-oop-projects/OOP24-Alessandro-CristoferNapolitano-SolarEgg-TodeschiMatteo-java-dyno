package it.unibo.javadyno.model.dyno.simulated.impl;

import org.junit.jupiter.api.Test;

import it.unibo.javadyno.model.dyno.simulated.api.SimulatedDyno;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for SimulatedDynoImpl.
 * It tests the initial state of the SimulatedDyno.
 */
class SimulatedDynoImplTest {
    /**
     * Test the beginning state of the SimulatedDyno.
     */
    @Test
    void testBeginning() {
        final SimulatedDyno dyno = new SimulatedDynoImpl();
        assertFalse(dyno.isRunning(), "The simulation shouldn't run at the start");
    }

    /**
     * Test the start and stop methods of the SimulatedDyno.
     */
    @Test
    void testStartAndStopSimulation() {
        final SimulatedDyno dyno = new SimulatedDynoImpl();
        dyno.startSimulation();
        assertTrue(dyno.isRunning(), "The simulation should run after start");
        dyno.stopSimulation();
        assertFalse(dyno.isRunning(), "The simulation should now be stopped");
    }
}

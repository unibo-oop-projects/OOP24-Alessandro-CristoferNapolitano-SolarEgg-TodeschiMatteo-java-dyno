package it.unibo.javadyno.model.dyno.simulated;

import org.junit.jupiter.api.Test;

import it.unibo.javadyno.controller.impl.ControllerImpl;
import it.unibo.javadyno.model.dyno.simulated.api.SimulatedDyno;
import it.unibo.javadyno.model.dyno.simulated.impl.SimulatedDynoImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;

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
        final SimulatedDyno dyno = new SimulatedDynoImpl(new ControllerImpl(), new CountDownLatch(1));
        assertFalse(dyno.isActive(), "The simulation shouldn't run at the start");
    }

    /**
     * Test the start and stop methods of the SimulatedDyno.
     */
    @Test
    void testStartAndStopSimulation() {
        final SimulatedDyno dyno = new SimulatedDynoImpl(new ControllerImpl(), new CountDownLatch(1));
        dyno.begin();
        assertTrue(dyno.isActive(), "The simulation should run after start");
        dyno.end();
        assertFalse(dyno.isActive(), "The simulation should now be stopped");
    }

    /**
     * Test the stop method when the simulation is not running.
     */
    @Test
    void testStopSimulationWhenNotStarted() {
        final SimulatedDyno dyno = new SimulatedDynoImpl(new ControllerImpl(), new CountDownLatch(1));
        dyno.end();
        assertFalse(dyno.isActive(), "The simulation should still not be running.");
    }

    /**
     * Test the isActive method of the SimulatedDyno.
     */
    @Test
    void testIsActive() {
        final SimulatedDyno dyno = new SimulatedDynoImpl(new ControllerImpl(), new CountDownLatch(1));
        assertFalse(dyno.isActive(), "The simulation should not be active initially");
        dyno.begin();
        assertTrue(dyno.isActive(), "The simulation should be active after starting");
        dyno.end();
        assertFalse(dyno.isActive(), "The simulation should not be active after stopping");
    }
}

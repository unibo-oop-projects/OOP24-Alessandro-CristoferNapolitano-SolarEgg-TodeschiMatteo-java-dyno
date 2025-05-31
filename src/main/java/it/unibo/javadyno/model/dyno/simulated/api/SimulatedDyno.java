package it.unibo.javadyno.model.dyno.simulated.api;

import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Simulated Dyno Interface.
 */
public interface SimulatedDyno extends Dyno {

    /**
     * Start the simulation.
     */
    void startSimulation();

    /**
     * Stop the simulation.
     */
    void stopSimulation();

    /**
     * Check if the simulation is running.
     *
     * @return true if running, false otherwise
     */
    boolean isRunning();
}

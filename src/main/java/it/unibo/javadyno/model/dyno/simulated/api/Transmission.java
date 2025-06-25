package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Transmission Interface.
 */
public interface Transmission {
    /**
     * Current gear ratio.
     *
     * @return gear ratio
     */
    double getCurrentRatio();

    /**
     * Selects up gear.
     */
    void shiftUp();

    /**
     * Selects down gear.
     */
    void shiftDown();

    /**
     * get current gear number
     * @return actual gear number
     */
    int getCurrentGear();
}

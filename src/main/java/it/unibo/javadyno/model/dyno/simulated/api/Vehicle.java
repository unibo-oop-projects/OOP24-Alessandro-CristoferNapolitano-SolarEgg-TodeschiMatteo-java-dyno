package it.unibo.javadyno.model.dyno.simulated.api;

import it.unibo.javadyno.model.data.api.RawData;

/**
 * Vehicle Interface.
 */
public interface Vehicle {
    /**
     * @param throttle gas aperture [0.0-1.0]
     */
    void setThrottle(double throttle);

    /**
     * @param deltatime simulation step of duration [s].
     */
    void update(double deltatime);

    /**
     * method to shift gear up.
     */
    void shiftUp();

    /**
     * method to shift gear down.
     */
    void shiftDown();

    /**
     * getter for current gear.
     *
     * @return current gear (one-based)
     */
    int getCurrentGear();

    /**
     * method to create raw data when called.
     *
     * @return raw data
     */
    RawData getRawData();
}

package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Vehicle Interface.
 */
public interface Vehicle {
    /**
     * 
     * @param throttle gas aperture [0.0-1.0]
     */
    void setThrottle(final double throttle);

    /**
     * @param deltatime simulation step of duration [s].
     */
    void update(final double deltatime);
}

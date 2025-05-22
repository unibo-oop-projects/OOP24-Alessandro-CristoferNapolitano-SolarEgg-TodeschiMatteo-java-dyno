package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Vehicle Interface.
 */
public interface Vehicle {
    /**
     * @return Engine.
     */
    Engine getEngine();

    /**
     * @return Transmission.
     */
    Transmission getTransmission();

    /**
     * @return Ecu.
     */
    Ecu getEcu();

    /**
     * @param dt step of simulation of duration dt(s).
     */
    default void update(final double dt) {

    }
}

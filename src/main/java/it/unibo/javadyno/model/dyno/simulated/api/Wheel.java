package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Wheels interface.
 */
public interface Wheel extends RotationalComponent, LoadModel {
    /**
     * Radius of the wheel.
     *
     * @return Radius in [m]
     */
    double getRadius();

    /**
     * Mass of the wheels.
     *
     * @return mass in [kg]
     */
    double getMass();
}

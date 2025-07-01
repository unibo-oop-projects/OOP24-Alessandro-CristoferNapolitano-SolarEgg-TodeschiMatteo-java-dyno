package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * interface for computing rolling resistance torque of a wheel.
 */
public interface FrictionModel {
    /**
     * compute the resistance torque using the angular velocity of the wheel.
     *
     * @param wheelOmega angular velocity of the wheel [rad/s]
     * @return friction torque applied to the wheel [Nm]
     */
    double computeFriction(double wheelOmega);
}

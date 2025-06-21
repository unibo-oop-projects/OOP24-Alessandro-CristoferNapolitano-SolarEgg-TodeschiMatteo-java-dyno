package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * interface to regulate inertial components influenced by the engine.
 */
public interface RotationalComponent {
    /**
     * Applies a torque and advances the component status by deltaTime.
     *
     * @param inputTorque torque applied to the component [Nm]
     * @param deltaTime step of simulation of duration in [s]
     */
    void update(double inputTorque, double deltaTime);

    /**
     * Angular velocity of the current component.
     *
     * @return angular velocity [rad/s]
     */
    double getAngularVelocity();
}

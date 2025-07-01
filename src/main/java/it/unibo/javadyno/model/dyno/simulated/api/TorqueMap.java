package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * interface used to generated torques given throttle and engine angular velocity.
 */
@FunctionalInterface
public interface TorqueMap {
    /**
     * method used to calculate torque gererated by the engine given throttle and engine angular velocity.
     *
     * @param throttle gas aperture [0.0-1.0]
     * @param omega angular velocity [rad/s]
     * @return generated torque [Nm]
     */
    double getTorque(double throttle, double omega);
}

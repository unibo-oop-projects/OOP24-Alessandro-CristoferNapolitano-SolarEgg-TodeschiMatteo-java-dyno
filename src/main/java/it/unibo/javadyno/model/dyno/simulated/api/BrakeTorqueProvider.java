package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * interface used as a provider of brake torque power
 * its intended use is to inject it onto the component you want to control
 * so that with an implementation you can implement a setter to change the status of the provider implementation.
 */
@FunctionalInterface
public interface BrakeTorqueProvider {
    /**
     * get brake torque.
     *
     * @return brake torque [Nm]
     */
    double getBrakeTorque();
}

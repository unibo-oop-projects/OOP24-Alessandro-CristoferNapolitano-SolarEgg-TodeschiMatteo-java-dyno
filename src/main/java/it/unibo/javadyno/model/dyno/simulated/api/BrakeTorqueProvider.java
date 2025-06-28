package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * 
 */
@FunctionalInterface
public interface BrakeTorqueProvider {
    /**
     * get brake torque
     * @return brake torque [Nm]
     */
    double getBrakeTorque();
}

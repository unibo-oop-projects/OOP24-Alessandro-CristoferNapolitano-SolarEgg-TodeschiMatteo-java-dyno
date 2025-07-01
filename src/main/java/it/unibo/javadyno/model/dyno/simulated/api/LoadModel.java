package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Model to manage load applied by rotational components to the engine.
 */
@FunctionalInterface
public interface LoadModel {
    /**
     * Compute load in function of the engine angular velocity.
     *
     * @param engineOmega engine angular velocity
     * @param gearRatio transmission gear ratio
     * @return load applied to the engine [Nm]
     */
    double getLoadTorque(double engineOmega, double gearRatio);
}

package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Model to manage load applied by rotational components to the engine.
 */
public interface LoadModel {
    /**
     * Compute load in function of the engine angular velocity.
     *
     * @param engineOmega engine angular velocity
     * @return load applied to the engine [Nm]
     */
    double getLoadTorque(double engineOmega);
}

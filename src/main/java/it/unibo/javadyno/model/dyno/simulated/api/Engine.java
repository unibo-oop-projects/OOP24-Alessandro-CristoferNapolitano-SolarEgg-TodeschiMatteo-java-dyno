package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Engine interface.
 */
public interface Engine {
    /**
     * Setter for gas aperture.
     *
     * @param throttle value between [0.0-1.0]
     */
    void setThrottle(double throttle);

    /**
     * Update engine status summing generated torque of the engine and 
     * applied torque to the engine.
     *
     * @param loadTorque sum of all loads applied to the engine[Nm] 
     * @param deltaTime step of duration [s]
     */
    void update(double loadTorque, double deltaTime);

    /**
     * Getter for current angular velocity of the engine.
     *
     * @return Angualr velocity in [rad/s]
     */
    double getAngularVelocity();

    /**
     * Getter for istant torque of the engine.
     *
     * @return Generated Torque in [Nm]
     */
    double getGeneratedTorque();

    /**
     * method used to regulate shifts of gears.
     *
     * @param newEngineOmega new engine omega [rad/s]
     */
    void setAngularVelocity(double newEngineOmega);

    /**
     * getter for engine temperature.
     *
     * @return engine temperature [°C]
     */
    double getEngineTemperature();

    /**
     * method for copying an instance of Engine.
     *
     * @return Engine copy
     */
    Engine copy();
}

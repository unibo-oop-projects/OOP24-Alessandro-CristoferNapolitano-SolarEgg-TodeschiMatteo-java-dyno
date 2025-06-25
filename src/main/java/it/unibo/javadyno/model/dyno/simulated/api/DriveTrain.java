package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Interface used to orchestrate power train of the vehicle updating the status of the components.
 */
public interface DriveTrain {

    /**
     * Advance status of the powertrain by (deltaTime) considering a certain gas aperture.
     *
     * @param throttle gas aperture, value between [0.0-1.0]
     * @param deltatime step of duration in [s]
     */
    void step(double throttle, double deltatime);

    /**
     * shift transmission gear up.
     */
    void shiftUp();

    /**
     * shift transmission gear down.
     */
    void shiftDown();

    /**
     * get engine angular velocity.
     *
     * @return engine angular velocity [rad/s]
     */
    double getEngineOmega();

    /**
     * get wheel angular velocity.
     *
     * @return wheel angular velocity [rad/s]
     */
    double getWheelOmega();

    /**
     * get current transmission gear.
     */
    int getCurrentGear();
    
}

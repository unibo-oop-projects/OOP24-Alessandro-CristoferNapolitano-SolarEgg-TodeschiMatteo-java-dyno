package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Simulated bench interface.
 */
public interface Bench {
    /**
     * @return the bench's RPM
     */
    int getRollerRPM();

    /**
     * @return the bench's angular speed
     */
    double getRollerAngularSpeed();

    /**
     * @return the bench's inertia momentum
     */
    double getRollerInertia();

    /**
     * @return the bench's radius
     */
    double getRollerRadius();

}

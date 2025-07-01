package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Interface used to realized to model temperature variation in an Engine.
 */
public interface TemperatureModel {
    /**
     * update of temperature model by deltaTime.
     *
     * @param deltaTime elapsed time of simulation step [s]
     */
    void update(double deltaTime);

    /**
     * getter for engine temperature.
     *
     * @return engine temperature [°C]
     */
    double getTemperature();
}

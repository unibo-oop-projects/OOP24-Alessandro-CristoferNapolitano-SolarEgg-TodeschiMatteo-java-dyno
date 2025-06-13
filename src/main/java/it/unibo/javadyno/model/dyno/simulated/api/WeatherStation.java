package it.unibo.javadyno.model.dyno.simulated.api;

/**
 * Gives enviroment data for the simulation
 * [note: this component is for specific future use to personalize engine behaviour].
 */
public interface WeatherStation {
    /**
     * Enviroment temperature.
     *
     * @return temperature in [°C]
     */
    double getTemperature();

    /**
     * Atmospheric pressure.
     *
     * @return pressure in [hPa]
     */
    double getPressure();

    /**
     * Humidity in air.
     *
     * @return value between [1-100]
     */
    int getHumidity();
}

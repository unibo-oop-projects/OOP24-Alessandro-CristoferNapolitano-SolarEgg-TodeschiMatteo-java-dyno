package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.WeatherStation;

/**
 * Weather Station to simulate various ambient conditions
 */
public class WeatherStationImpl implements WeatherStation{
    private final double temperature;
    private final int  pressure;
    private final int humidity;

    /**
     * simple constructor for WeatherStationImpl.
     *
     * @param temperature value in [°C]
     * @param pressure value in [kPa]
     * @param humidity value between [0-100]
     */
    public WeatherStationImpl(final double temperature, final int pressure, final int humidity) {
        
        this.temperature = temperature;
        this.pressure = pressure;
        if (humidity < 0 || humidity > 100) {
            throw new IllegalArgumentException("humidity must be: h >= 0 & h < 100");
        }
        this.humidity = humidity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTemperature() {
        return this.temperature;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPressure() {
        return this.pressure;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHumidity() {
        return this.humidity;
    }
    
}

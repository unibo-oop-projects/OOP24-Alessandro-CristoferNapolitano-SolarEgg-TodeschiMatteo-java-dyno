package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.TemperatureModel;

/**
 * Simple temperature model based on a linear heating and exponential cooling
 */
public class SimpleTemperatureModel implements TemperatureModel{
    private double temperature; // °C
    private final double heatGain; // [°C/(W·s)]
    private final double coolingFactor; // [1/s]

    /**
     * 
     * @param temperature initial temperature [°C]
     * @param heatGain heat gain [°C/(W·s)] [must be >= 0]
     * @param coolingFactor cooling coefficient [1/s] [must be >= 0]
     */
    public SimpleTemperatureModel(double temperature, double heatGain, double coolingFactor) {
        if (heatGain < 0) {
            throw new IllegalArgumentException("heatGain >= 0");
        }
        if (coolingFactor < 0) {
            throw new IllegalArgumentException("coolingFactor >= 0");
        }
        this.temperature   = temperature;
        this.heatGain      = heatGain;
        this.coolingFactor = coolingFactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(double generatedPower, double ambientTemp, double deltaTime) {
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("deltaTime>0");
        }
        //linear heating
        double dT_heat = heatGain * generatedPower * deltaTime;
        //exponential cooling
        double dT_cool = -coolingFactor * (temperature - ambientTemp) * deltaTime;
        temperature += dT_heat + dT_cool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTemperature() {
        return temperature;
    }
}

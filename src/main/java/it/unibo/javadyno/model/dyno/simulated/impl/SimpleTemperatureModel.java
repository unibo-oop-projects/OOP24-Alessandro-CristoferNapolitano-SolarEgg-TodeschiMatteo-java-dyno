package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.TemperatureModel;

/**
 * Simple temperature model based on a linear heating and exponential cooling.
 */
public class SimpleTemperatureModel implements TemperatureModel {
    private double temperature; // °C
    private final double heatGain; // [°C/(W·s)]
    private final double coolingFactor; // [1/s]

    /**
     * simple constructor for SimpleTemperatureModel.
     *
     * @param temperature initial temperature [°C]
     * @param heatGain heat gain [°C/(W·s)] [must be >= 0]
     * @param coolingFactor cooling coefficient [1/s] [must be >= 0]
     */
    public SimpleTemperatureModel(final double temperature, final double heatGain, final double coolingFactor) {
        if (heatGain < 0) {
            throw new IllegalArgumentException("heatGain >= 0");
        }
        if (coolingFactor < 0) {
            throw new IllegalArgumentException("coolingFactor >= 0");
        }
        this.temperature = temperature;
        this.heatGain = heatGain;
        this.coolingFactor = coolingFactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double generatedPower, final double ambientTemp, final double deltaTime) {
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("deltaTime>0");
        }
        //linear heating
        final double dTHeat = heatGain * generatedPower * deltaTime;
        //exponential cooling
        final double dTCool = (-coolingFactor) * (temperature - ambientTemp) * deltaTime;
        System.out.println("Thread: " + Thread.currentThread().getName());
        System.out.println("Heating: " + dTHeat + " Cooling: " + dTCool);
        temperature += (dTHeat + dTCool) / 100.0;
        System.out.println("New temperature: " + temperature);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTemperature() {
        return temperature;
    }
}

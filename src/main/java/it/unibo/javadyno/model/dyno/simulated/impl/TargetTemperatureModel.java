package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.TemperatureModel;

/**
 * temperature model that approaces a target temperature.
 */
public class TargetTemperatureModel implements TemperatureModel {
    private final double targetTemperature;  // °C
    private final double timeConstant;       // [s]
    private double temperature;              // current [°C]

    /**
     * simple constructor for TargetTemperatureModel.
     *
     * @param initialTemperature  starting temperature [°C]
     * @param targetTemperature   target temperature [°C]
     * @param timeConstant        time constant [s], [must be > 0]
     */
    public TargetTemperatureModel(double initialTemperature, double targetTemperature, double timeConstant) {
        if (timeConstant <= 0) {
            throw new IllegalArgumentException("timeConstant must be > 0");
        }
        this.temperature = initialTemperature;
        this.targetTemperature = targetTemperature;
        this.timeConstant = timeConstant;
    }
    
    /**
     * Advances the model by dt seconds
     * simply moves T toward targetTemperature with time constant.
     *
     * @param deltaTime timestep dt [s]; must be > 0
     */
    @Override
    public void update(double deltaTime) {
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("deltaTime must be > 0");
        }
        // exponential approach
        double dT = (targetTemperature - temperature) / timeConstant * deltaTime;
        temperature += dT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTemperature() {
        return this.temperature;
    }
    
}

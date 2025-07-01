package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.TemperatureModel;

/**
 * temperature model that approaces a target temperature.
 */
public class TargetTemperatureModel implements TemperatureModel {
    private final double targetTemperature;  // °C
    private final double timeCoeff;       // [s]
    private double temperature;              // current [°C]

    /**
     * simple constructor for TargetTemperatureModel.
     *
     * @param initialTemperature  starting temperature [°C]
     * @param targetTemperature   target temperature [°C]
     * @param timeCoeff        time coeff [s], [must be > 0]
     */
    public TargetTemperatureModel(final double initialTemperature, final double targetTemperature, final double timeCoeff) {
        if (timeCoeff <= 0) {
            throw new IllegalArgumentException("timeConstant must be > 0");
        }
        this.temperature = initialTemperature;
        this.targetTemperature = targetTemperature;
        this.timeCoeff = timeCoeff;
    }

    /**
     * Advances the model by dt seconds
     * simply moves T toward targetTemperature with time constant.
     *
     * @param deltaTime timestep dt [s]; must be > 0
     */
    @Override
    public void update(final double deltaTime) {
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("deltaTime must be > 0");
        }
        // exponential approach
        final double dT = (targetTemperature - temperature) / timeCoeff * deltaTime;
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

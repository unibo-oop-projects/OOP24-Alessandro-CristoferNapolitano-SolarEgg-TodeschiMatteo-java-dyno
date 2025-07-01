package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.LoadModel;

/**
 * load model to recreate wheel rolling resistance.
 */
public class RollingResistance implements LoadModel {
    private final double coefficient; // [Nm * rad/s]

    /**
     * simple constructor for rolling resistance.
     *
     * @param coefficient rolling resistance coefficient [Nm * rad/s]
     */
    public RollingResistance(final double coefficient) {
        if (coefficient < 0) {
            throw new IllegalArgumentException("coefficient must be > 0");
        }
        this.coefficient = coefficient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLoadTorque(final double engineOmega, final double gearRatio) {
        final double wheelOmega = engineOmega * gearRatio;
        final double friction = coefficient * wheelOmega;
        return friction / gearRatio;
    }
}

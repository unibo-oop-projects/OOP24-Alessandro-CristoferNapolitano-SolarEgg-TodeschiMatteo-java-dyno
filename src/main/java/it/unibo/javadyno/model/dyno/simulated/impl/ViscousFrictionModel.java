package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.FrictionModel;

/**
 * FrictionModel with linear viscous resistance
 * T = coeff * wheelOmega.
 */
public class ViscousFrictionModel implements FrictionModel {
    private final double coeff;

    /**
     * simple constructor for ViscousFrictionModel.
     *
     * @param coeff value must be >= 0
     */
    public ViscousFrictionModel(final double coeff) {
        if (coeff < 0) {
            throw new IllegalArgumentException("coefficient must be >= 0");
        }
        this.coeff = coeff;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double computeFriction(final double wheelOmega) {
        return this.coeff * wheelOmega;
    }
}

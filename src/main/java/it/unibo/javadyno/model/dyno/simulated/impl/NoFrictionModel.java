package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.FrictionModel;

/**
 * FrictionModel without resistance force.
 */
public class NoFrictionModel implements FrictionModel {

    /**
     * {@inheritDoc}
     */
    @Override
    public double computeFriction(final double wheelOmega) {
        return 0;
    }
}

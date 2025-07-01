package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.Objects;

import it.unibo.javadyno.model.dyno.simulated.api.BrakeTorqueProvider;
import it.unibo.javadyno.model.dyno.simulated.api.LoadModel;

/**
 * LoadModel that applies brake torque from BrakeTorqueProvider.
 */
public class BenchLoad implements LoadModel {
    private final BrakeTorqueProvider provider;

    /**
     * simple constructor for BenchLoad.
     *
     * @param provider non null provider
     */
    public BenchLoad(final BrakeTorqueProvider provider) {
        this.provider = Objects.requireNonNull(provider, "provider needed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLoadTorque(final double engineOmega, final double gearRatio) {
        return provider.getBrakeTorque();
    }
}

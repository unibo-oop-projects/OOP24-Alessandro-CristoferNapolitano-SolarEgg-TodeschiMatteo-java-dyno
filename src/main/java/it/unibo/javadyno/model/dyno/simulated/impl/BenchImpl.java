package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.Bench;

/**
 * Simulated bench implementation.
 */
public class BenchImpl implements Bench {

    private final int rpm;
    private final double rollerRadius;
    private final double rollerInertia;
    private final double angularSpeed;

    /**
     * Constructor of the bench implementation.
     *
     * @param rollerRadius the roller radius of the simulated bench
     * @param rollerInertia the roller inertia of the simulated bench
     */
    public BenchImpl(final double rollerRadius, final double rollerInertia) {
        this.rpm = 0;
        this.rollerRadius = rollerRadius;
        this.rollerInertia = rollerInertia;
        this.angularSpeed = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRollerRPM() {
        return this.rpm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRollerAngularSpeed() {
        return this.angularSpeed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRollerInertia() {
        return this.rollerInertia;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRollerRadius() {
        return this.rollerRadius;
    }
}

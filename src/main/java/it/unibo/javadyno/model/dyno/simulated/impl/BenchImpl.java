package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.Bench;

/**
 * Simulated bench implementation.
 */
public class BenchImpl implements Bench {
    private static final double DEFAULT_ROLLER_INERTIA = 0.01;
    private static final double DEFAULT_ROLLER_RADIUS = 0.1;

    private final int rpm;
    private final double rollerRadius;
    private final double rollerInertia;
    private final double angularSpeed;

    /**
     * Default constructor of the bench implementation.
     * Initializes the bench with default values.
     */
    public BenchImpl() {
        this.rpm = 0;
        this.rollerRadius = DEFAULT_ROLLER_RADIUS;
        this.rollerInertia = DEFAULT_ROLLER_INERTIA;
        this.angularSpeed = 0;
    }

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

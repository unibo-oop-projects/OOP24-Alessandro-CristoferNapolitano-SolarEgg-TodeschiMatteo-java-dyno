package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.Objects;

import it.unibo.javadyno.model.dyno.simulated.api.FrictionModel;
import it.unibo.javadyno.model.dyno.simulated.api.Wheel;

/**
 * Simple implementation of a Wheel
 */
public class DynamicWheel implements Wheel{
    private final double inertia;
    private final double radius;
    private final double mass;
    private final FrictionModel frictionModel;
    private double wheelOmega;

    /**
     * 
     * @param radius
     * @param mass
     * @param frictionModel
     */
    public DynamicWheel(double radius, double mass, FrictionModel frictionModel) {
        if (mass <=0) {
            throw new IllegalArgumentException("mass must be > 0");
        }
        if (radius <=0) {
            throw new IllegalArgumentException("radius must be > 0");
        }
        this.inertia = mass * radius * radius;
        this.radius = radius;
        this.mass = mass;
        this.frictionModel = Objects.requireNonNull(frictionModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(double inputTorque, double deltaTime) {
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("deltaTime must be >= 0");
        }
        double frictionTorque = this.frictionModel.computeFriction(wheelOmega);
        double netTorque = inputTorque - frictionTorque;
        wheelOmega = wheelOmega + (netTorque / inertia) * deltaTime;
        if (wheelOmega < 0) {
            wheelOmega = 0;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAngularVelocity() {
        return this.wheelOmega;
    }

    /**
     * TODO: refactoring needed for loadModel interface, in this case engineOmega remains unused
     */
    @Override
    public double getLoadTorque(double engineOmega, double gearRatio) {
        double frictionWheel = frictionModel.computeFriction(wheelOmega);
        return frictionWheel / gearRatio;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRadius() {
        return this.radius;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMass() {
        return this.mass;
    }
    
}

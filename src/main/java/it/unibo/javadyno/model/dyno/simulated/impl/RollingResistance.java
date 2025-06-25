package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.LoadModel;

public class RollingResistance implements LoadModel{
    private final double coefficient; // [Nm * rad/s]

    public RollingResistance(double coefficient) {
        if (coefficient < 0) {
            throw new IllegalArgumentException("coefficient must be > 0");
        }
        this.coefficient = coefficient;
    }

    @Override
    public double getLoadTorque(double engineOmega, double gearRatio) {
        double wheelOmega = engineOmega * gearRatio;
        double friction = coefficient * wheelOmega;
        return friction / gearRatio;
    }
    
}

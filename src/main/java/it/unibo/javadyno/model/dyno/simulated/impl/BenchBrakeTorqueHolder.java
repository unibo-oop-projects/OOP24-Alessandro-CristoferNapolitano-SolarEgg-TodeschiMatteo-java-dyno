package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.BrakeTorqueProvider;

/**
 * a mutable holder of brake torque power for the BenchLoad.
 */
public class BenchBrakeTorqueHolder implements BrakeTorqueProvider{
    private double currentBrakeTorque;
    
    /**
     * {@inheritDoc }
     */
    @Override
    public double getBrakeTorque() {
        return currentBrakeTorque;
    }

    /**
     * setter for bench brake torque value must be >= 0.
     *
     * @param benchBrakeTorque bench brake torque, value must be >= 0
     */
    public void setBrakeTorque(double benchBrakeTorque) {
        if (benchBrakeTorque < 0) {
            throw new IllegalArgumentException("brake torque must be >= 0");
        }
        this.currentBrakeTorque = benchBrakeTorque;
    }
    
}

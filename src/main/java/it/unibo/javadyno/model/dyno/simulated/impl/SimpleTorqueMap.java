package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.TorqueMap;

/**
 * creation of simple torque map for an engine
 * given by the formula:
 * T = torque
 * th = throttle
 * Tb = base torque
 * Trs = torque per rads
 * w = omega (engine angular velocity)
 *  T= th * (Tb + Trs * w ).
 */
public class SimpleTorqueMap implements TorqueMap {
    private final double baseTorque;
    private final double torquePerRad;

    /**
     * constructor for SimpleTorqueMap.
     *
     * @param baseTorque base torque [Nm]
     * @param torquePerRad torque generated per radiant [Nm * rad]
     */
    public SimpleTorqueMap(final double baseTorque, final double torquePerRad) {
        if (baseTorque < 0 || torquePerRad < 0) {
            throw new IllegalArgumentException("baseTorque and torquePerRad must be >=0");
        }
        this.baseTorque = baseTorque;
        this.torquePerRad = torquePerRad;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTorque(final double throttle, final double omega) {
        if (throttle < 0.0 || throttle > 1.0) {
            throw new IllegalArgumentException("throttle must be between 0.0 and 1.0");
        }
        return throttle * (baseTorque + this.torquePerRad * omega);
    }

}

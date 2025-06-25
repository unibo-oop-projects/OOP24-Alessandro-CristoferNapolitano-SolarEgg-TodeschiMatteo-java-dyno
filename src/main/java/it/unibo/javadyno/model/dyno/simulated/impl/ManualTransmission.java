package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.Transmission;

public class ManualTransmission implements Transmission{
    private final double[] gearRatio;
    private int currentGearIndex;

    /**
     * simple constructor for a ManualTransmission.
     *
     * @param gearRatio gear ratio
     */
    public ManualTransmission(double[] gearRatio) {
        if (gearRatio == null || gearRatio.length == 0) {
            throw new IllegalArgumentException("Transmission must have at least one gear");
        }
        this.gearRatio = gearRatio.clone();
        this.currentGearIndex = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getCurrentRatio() {
        return gearRatio[currentGearIndex];
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public void shiftUp() {
        if (currentGearIndex < gearRatio.length - 1) {
            currentGearIndex++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shiftDown() {
        if (currentGearIndex > 0) {
            currentGearIndex--;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentGear() {
        return currentGearIndex + 1;
    }
    
}

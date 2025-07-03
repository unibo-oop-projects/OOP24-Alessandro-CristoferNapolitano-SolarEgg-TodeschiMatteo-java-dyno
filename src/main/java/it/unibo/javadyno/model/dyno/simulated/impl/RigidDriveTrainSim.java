package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.List;
import java.util.Objects;

import it.unibo.javadyno.model.dyno.simulated.api.DriveTrain;
import it.unibo.javadyno.model.dyno.simulated.api.Engine;
import it.unibo.javadyno.model.dyno.simulated.api.LoadModel;
import it.unibo.javadyno.model.dyno.simulated.api.Transmission;

/**
 * implementation that holds and manages: 
 * Engine, Transmission, list of LoadModel, deltaTime.
 */
public class RigidDriveTrainSim implements DriveTrain {
    private final Engine engine;
    private final Transmission transmission;
    private final List<LoadModel> loads;

    /**
     * simple constructor for RigidDriveTrainSim.
     *
     * @param engine non null Engine
     * @param transmission non null Transmission
     * @param loads list of LoadModel for the drive train
     */
    public RigidDriveTrainSim(final Engine engine, final Transmission transmission, final List<LoadModel> loads) {
        this.engine = Objects.requireNonNull(engine.copy());
        this.transmission = Objects.requireNonNull(transmission.copy());
        this.loads = List.copyOf(loads);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void step(final double throttle, final double deltaTime) {
        engine.setThrottle(throttle);
        final double engineOmega = engine.getAngularVelocity();
        final double gearRatio = transmission.getCurrentRatio();
        final double totalLoad = loads.stream()
            .mapToDouble(lm -> lm.getLoadTorque(engineOmega, gearRatio))
            .sum();
        engine.update(totalLoad, deltaTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getEngineOmega() {
        return engine.getAngularVelocity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getEngineTemperature() {
        return engine.getEngineTemperature();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getWheelOmega() {
        return engine.getAngularVelocity() * transmission.getCurrentRatio();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGeneratedTorque() {
        return engine.getGeneratedTorque();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shiftDown() {
        final double oldRatio = transmission.getCurrentRatio();
        final double oldWheelOmega = engine.getAngularVelocity() * oldRatio;

        transmission.shiftUp();

        final double newRatio = transmission.getCurrentRatio();
        final double newEngineOmega = oldWheelOmega / newRatio;
        engine.setAngularVelocity(newEngineOmega);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shiftUp() {
        final double oldRatio = transmission.getCurrentRatio();
        final double oldWheelOmega = engine.getAngularVelocity() * oldRatio;

        transmission.shiftDown();

        final double newRatio = transmission.getCurrentRatio();
        final double newEngineOmega = oldWheelOmega / newRatio;
        engine.setAngularVelocity(newEngineOmega);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentGear() {
        return transmission.getCurrentGear();
    }
}

package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.List;
import java.util.Objects;

import it.unibo.javadyno.model.dyno.simulated.api.DriveTrain;
import it.unibo.javadyno.model.dyno.simulated.api.Engine;
import it.unibo.javadyno.model.dyno.simulated.api.LoadModel;
import it.unibo.javadyno.model.dyno.simulated.api.Transmission;

public class RigidDriveTrainSim implements DriveTrain{
    private final Engine engine;
    private final Transmission transmission;
    private final List<LoadModel> loads;
    private final double deltaTime;

    public RigidDriveTrainSim(Engine engine, Transmission transmission, List<LoadModel> loads,
            double deltaTime) {
        this.engine = Objects.requireNonNull(engine);
        this.transmission = Objects.requireNonNull(transmission);
        this.loads = List.copyOf(loads);
        if (deltaTime <= 0) {
            throw new IllegalArgumentException();
        }
        this.deltaTime = deltaTime;
    }

    @Override
    public void step(double throttle, double deltatime) {
        engine.setThrottle(throttle);
        double engineOmega = engine.getAngularVelocity();
        double gearRatio = transmission.getCurrentRatio();
        double totalLoad = loads.stream()
            .mapToDouble(lm -> lm.getLoadTorque(engineOmega, gearRatio))
            .sum();
        engine.update(totalLoad, deltaTime);
    }
    
    public double getEngineOmega() {
        return engine.getAngularVelocity();
    }

    public double getEngineTemperature() {
        return engine.getEngineTemperature();
    }

    public double getWheelOmega() {
        return engine.getAngularVelocity() * transmission.getCurrentRatio();
    }

    public double getGeneratedTorque() {
        return engine.getGeneratedTorque();
    }

    //TODO : creation of a clutch component to synchronize engine and wheel speed
    public void shiftDown() {
        double oldRatio  = transmission.getCurrentRatio();
        double oldWheelOmega = engine.getAngularVelocity() * oldRatio;

        transmission.shiftUp();

        double newRatio     = transmission.getCurrentRatio();
        double newEngineOmega   = oldWheelOmega / newRatio;
        engine.setAngularVelocity(newEngineOmega);
    }

    //TODO : creation of a clutch component to synchronize engine and wheel speed
    public void shiftUp() {
        double oldRatio  = transmission.getCurrentRatio();
        double oldWheelOmega = engine.getAngularVelocity() * oldRatio;

        transmission.shiftDown();

        double newRatio     = transmission.getCurrentRatio();
        double newEngineOmega   = oldWheelOmega / newRatio;
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
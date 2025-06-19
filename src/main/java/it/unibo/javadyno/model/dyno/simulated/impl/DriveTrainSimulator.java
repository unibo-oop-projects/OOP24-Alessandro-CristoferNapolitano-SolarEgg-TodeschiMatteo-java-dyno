package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.List;
import java.util.Objects;

import it.unibo.javadyno.model.dyno.simulated.api.DriveTrain;
import it.unibo.javadyno.model.dyno.simulated.api.Engine;
import it.unibo.javadyno.model.dyno.simulated.api.LoadModel;
import it.unibo.javadyno.model.dyno.simulated.api.RotationalComponent;
import it.unibo.javadyno.model.dyno.simulated.api.Transmission;
import it.unibo.javadyno.model.dyno.simulated.api.Wheel;

/**
 * Simulator that coordinates the distribution of forces in the system.
 */
public class DriveTrainSimulator implements DriveTrain {
    private final Engine engine;
    private final Transmission transmission;
    private final List<LoadModel> loadModels;
    private final List<Wheel> wheels;
    private final List<RotationalComponent> rotationalComponents;
    private final double deltaTime;

    /**
     * unique constructor for DriveTrainSimulator.
     *
     * @param engine non null Engine
     * @param transmission non null Transmission
     * @param loadModels non null loadModel
     * @param wheels non null wheels
     * @param rotationalComponents non null rotational components
     * @param deltaTime delta time must be > 0
     */
    public DriveTrainSimulator(final Engine engine, final Transmission transmission, final List<LoadModel> loadModels,
            final List<Wheel> wheels, final List<RotationalComponent> rotationalComponents,
            final double deltaTime) {
        this.engine = Objects.requireNonNull(engine);
        this.transmission = Objects.requireNonNull(transmission);
        this.loadModels = Objects.requireNonNull(loadModels);
        this.wheels = Objects.requireNonNull(wheels);
        this.rotationalComponents = Objects.requireNonNull(rotationalComponents);
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("delta time must be > 0 ");
        }
        this.deltaTime = deltaTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void step(final double throttle, final double deltatime) {
        engine.setThrottle(throttle);

        final double engineOmega = engine.getAngularVelocity();
        final double gearRatio = transmission.getCurrentRatio();
        final double totalLoads = loadModels.stream()
            .mapToDouble(lm -> lm.getLoadTorque(engineOmega, gearRatio))
            .sum();

        engine.update(totalLoads, deltatime);

        final double torqueOut = engine.getGeneratedTorque() * gearRatio;
        wheels.stream().forEach(wheel -> wheel.update(torqueOut, deltatime));
        rotationalComponents.stream().forEach(rc -> rc.update(torqueOut, deltatime));
    }

    /**
     * step of simulation with default deltaTime.
     *
     * @param throttle gas aperture [0-1]
     */
    public void step(final double throttle) {
        this.step(throttle, this.deltaTime);
    }
}

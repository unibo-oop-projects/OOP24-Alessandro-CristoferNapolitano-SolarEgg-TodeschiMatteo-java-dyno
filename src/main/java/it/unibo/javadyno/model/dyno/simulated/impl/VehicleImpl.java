package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.Optional;

import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.dyno.simulated.api.DriveTrain;
import it.unibo.javadyno.model.dyno.simulated.api.Vehicle;
import it.unibo.javadyno.model.dyno.simulated.api.WeatherStation;

/**
 * Implementation of a vehicle with a drive train, wheel, injection of a weatherstation.
 */
public final class VehicleImpl implements Vehicle {
    private final DriveTrain drivetrain;
    private final WeatherStation weatherStation;
    private final double wheelRadius;
    private double currentThrottle;

    /**
     * simple constructor for VehicleImpl.
     *
     * @param drivetrain DriveTrain implementation
     * @param weather WeatherStation, used to calculate alternatives drivetrain implementations
     * @param wheelRadius wheel radius, used to compute vehicle speed [m]
     * @param currentThrottle starting throttle position
     */
    public VehicleImpl(final DriveTrain drivetrain, final WeatherStation weather,
            final double wheelRadius, final double currentThrottle) {
        this.drivetrain = java.util.Objects.requireNonNull(drivetrain, "drivetrain");
        this.weatherStation = java.util.Objects.requireNonNull(weather, "weather");
        if (wheelRadius <= 0) {
            throw new IllegalArgumentException("wheelRadius must be > 0");
        }
        this.wheelRadius = wheelRadius;
        this.setThrottle(currentThrottle);
    }

    @Override
    public void setThrottle(final double throttle) {
        if (throttle < 0.0 || throttle > 1.0) {
            throw new IllegalArgumentException("throttle must be between [0-1]");
        }
        this.currentThrottle = throttle;
    }

    @Override
    public void update(final double deltatime) {
        drivetrain.step(currentThrottle, deltatime);
    }

    @Override
    public void shiftUp() {
        drivetrain.shiftUp();
    }

    @Override
    public void shiftDown() {
        drivetrain.shiftDown();
    }

    @Override
    public int getCurrentGear() {
        return drivetrain.getCurrentGear();
    }

    @Override
    public RawData getRawData() {
        final double engineOmega = drivetrain.getEngineOmega();
        final int engineRpm = (int) Math.round(engineOmega * 60.0 / (2 * Math.PI));

        final double engineTemp = drivetrain.getEngineTemperature();

        final double engineTorque = drivetrain.getGeneratedTorque();
        final double wheelOmega = drivetrain.getWheelOmega();
        //TorqueWheel = TorqueEngine / gear
        final double wheelTorque = engineOmega == 0 ? 0 : engineTorque * (wheelOmega / engineOmega);

        final int speedKmh = (int) Math.round(wheelOmega * wheelRadius * 3.6);

        final double throttle = currentThrottle;

        final int ambientTemperature = (int) weatherStation.getTemperature();
        final int ambientPressure = weatherStation.getPressure();
        final int ambientHumidity = weatherStation.getHumidity();

        return RawData.builder()
                .engineRPM(Optional.of(engineRpm))
                .engineTemperature(Optional.of(engineTemp))
                .torque(Optional.of(wheelTorque))
                .vehicleSpeed(Optional.of(speedKmh))
                .throttlePosition(Optional.of(throttle))
                .ambientAirTemperature(Optional.of(ambientTemperature))
                .ambientHumidity(Optional.of(ambientHumidity))
                .baroPressure(Optional.of(ambientPressure))
                .build();
    }
}

package it.unibo.javadyno.model.dyno.simulated.impl;

import it.unibo.javadyno.model.dyno.simulated.api.DriveTrain;
import it.unibo.javadyno.model.dyno.simulated.api.Vehicle;
import it.unibo.javadyno.model.dyno.simulated.api.WeatherStation;

public class VehicleImpl implements Vehicle{
    private final DriveTrain drivetrain;
    private final WeatherStation weather;
    private final double wheelRadius;
    private double currentThrottle;

    /**
     * 
     * @param drivetrain
     * @param weather
     * @param wheelRadius wheel radius, used to compute vehicle speed [m]
     * @param currentThrottle
     */
    public VehicleImpl(DriveTrain drivetrain, WeatherStation weather,
            double wheelRadius, double currentThrottle) {
        this.drivetrain   = java.util.Objects.requireNonNull(drivetrain, "drivetrain");
        this.weather      = java.util.Objects.requireNonNull(weather,  "weather");
        if (wheelRadius <= 0) throw new IllegalArgumentException("wheelRadius must be > 0");
        this.wheelRadius  = wheelRadius;
    }

    @Override
    public void setThrottle(double throttle) {
        if (throttle < 0.0 || throttle > 1.0) {
            throw new IllegalArgumentException("throttle must be between [0-1]");
        }
        this.currentThrottle = throttle;
    }

    @Override
    public void update(double deltatime) {
        drivetrain.step(currentThrottle,deltatime);
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

    /* TODO :
    VehicleRawData getVehicleRawData() {

    }
    */
    
}

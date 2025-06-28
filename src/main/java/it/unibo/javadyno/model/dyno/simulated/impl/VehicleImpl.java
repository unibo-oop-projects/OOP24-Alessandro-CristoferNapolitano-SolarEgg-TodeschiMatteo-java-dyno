package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.Optional;

import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.dyno.simulated.api.DriveTrain;
import it.unibo.javadyno.model.dyno.simulated.api.Vehicle;
import it.unibo.javadyno.model.dyno.simulated.api.WeatherStation;

public class VehicleImpl implements Vehicle{
    private final DriveTrain drivetrain;
    private final WeatherStation weatherStation;
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
        this.drivetrain = java.util.Objects.requireNonNull(drivetrain, "drivetrain");
        this.weatherStation  = java.util.Objects.requireNonNull(weather,  "weather");
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

    @Override
    public RawData getRawData() {
        //engine RPM [rev/min]
        double engineOmega = drivetrain.getEngineOmega();
        int engineRpm = (int) Math.round(engineOmega * 60.0 / (2 * Math.PI));

        //engine temperature [°C]
        double engineTemp = drivetrain.getEngineTemperature();

        //compute wheel torque [Nm] from engine torque
        double engineTorque = drivetrain.getGeneratedTorque();
        double wheelOmega = drivetrain.getWheelOmega();
        //TorqueWheel = TorqueEngine / gear
        double wheelTorque = engineOmega == 0 ? 0 : engineTorque * (wheelOmega / engineOmega);

        //vehicle speed [km/h]
        int speedKmh = (int) Math.round(wheelOmega * wheelRadius * 3.6);

        //gas aperture [0-1]
        double throttle = currentThrottle;

        //ambient data
        int ambientTemperature = (int)weatherStation.getTemperature();
        int ambientPressure = weatherStation.getPressure();
        //int ambientHumidity = weatherStation.getHumidity();
    
        // TODO : change pressure to kPa
        // TODO : change in rawdata ambienttemp in double

        return RawData.builder()
                .engineRPM(Optional.of(engineRpm))
                .engineTemperature(Optional.of(engineTemp))
                .torque(Optional.of(wheelTorque))
                .vehicleSpeed(Optional.of(speedKmh))
                .throttlePosition(Optional.of(throttle))
                .ambientAirTemperature(Optional.of(ambientTemperature))
                .baroPressure(Optional.of(ambientPressure))
                .build();
    }
}

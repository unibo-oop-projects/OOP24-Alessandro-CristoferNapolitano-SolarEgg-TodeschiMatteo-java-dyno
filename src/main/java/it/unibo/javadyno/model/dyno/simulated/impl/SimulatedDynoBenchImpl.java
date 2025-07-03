package it.unibo.javadyno.model.dyno.simulated.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.api.UserSettings;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Test implementation of a Real Dyno for simulation purposes.
 * This class generates realistic dyno data with torque and engine RPM
 */
public final class SimulatedDynoBenchImpl implements Dyno {
    private static final Integer INITIAL_ENGINE_RPM = 1000;
    private static final Double INITIAL_TORQUE = 50.0;
    private static final Double INITIAL_ENGINE_TEMPERATURE = 85.0;
    private static final Double INITIAL_THROTTLE_POSITION = 10.0;
    private static final double RPM_INCREASE_FACTOR = 1.03;
    private static final double MAX_TORQUE = 400.0;
    private static final double TORQUE_PEAK_RPM = 3500.0;
    private static final double TORQUE_VARIATION = 15.0;
    private static final double TEMP_INCREASE_RATE = 0.1;
    private static final double MAX_ENGINE_TEMPERATURE = 105.0;
    private static final double MIN_ENGINE_TEMPERATURE = 80.0;
    private static final double THROTTLE_INCREASE_RATE = 1.5;
    private static final double MAX_THROTTLE_POSITION = 100.0;
    private static final double MIN_THROTTLE_POSITION = 5.0;
    private static final double TRANSMISSION_RATIO = 0.3;
    private static final double WHEEL_RADIUS = 0.3;
    private static final int MIN_DELAY_MILLIS = 200;
    private static final int MAX_DELAY_MILLIS = 400;
    private final int maxEngineRPM;
    private final Random rand = new Random();
    private RawData prevRawData;
    private boolean isActive;

    /**
     * Constructor for SimulatedDynoBenchImpl.
     *
     * @param userSettings the user settings containing the maximum RPM for the simulation
     */
    public SimulatedDynoBenchImpl(final UserSettings userSettings) {
        maxEngineRPM = (int) userSettings.getSimulationMaxRPM();
        this.prevRawData = RawData.builder()
                .engineRPM(Optional.of(INITIAL_ENGINE_RPM))
                .torque(Optional.of(INITIAL_TORQUE))
                .engineTemperature(Optional.of(INITIAL_ENGINE_TEMPERATURE))
                .throttlePosition(Optional.of(INITIAL_THROTTLE_POSITION))
                .timestamp(Optional.of(Instant.now()))
                .build();
    }

    @Override
    public RawData getRawData() {
        if (!isActive) {
            return prevRawData;
        }

        final int currentRpm = this.prevRawData.engineRPM().get();
        final Integer newRpm;
        newRpm = Math.min(maxEngineRPM, (int) (currentRpm * RPM_INCREASE_FACTOR));
        if (newRpm >= maxEngineRPM) {
            isActive = false;
        }

        final double rpmRatio = newRpm / TORQUE_PEAK_RPM;
        final double baseTorque;
        if (rpmRatio <= 1.0) {
            baseTorque = INITIAL_TORQUE + (MAX_TORQUE - INITIAL_TORQUE) * Math.sin(rpmRatio * Math.PI / 2);
        } else {
            baseTorque = MAX_TORQUE * Math.cos((rpmRatio - 1.0) * Math.PI / 4);
        }

        final double torqueVariation = (rand.nextDouble() - 0.5) * TORQUE_VARIATION;
        final Double newTorque = Math.max(10.0, baseTorque + torqueVariation);

        final double currentTemp = this.prevRawData.engineTemperature().get();
        final double tempIncrease = (newRpm > 4000) ? TEMP_INCREASE_RATE : -TEMP_INCREASE_RATE * 0.3;
        final Double newTemperature = Math.max(MIN_ENGINE_TEMPERATURE, 
            Math.min(MAX_ENGINE_TEMPERATURE, currentTemp + tempIncrease + rand.nextGaussian() * 0.5));

        final double currentThrottle = this.prevRawData.throttlePosition().get();
        final double throttleChange = THROTTLE_INCREASE_RATE;
        final Double newThrottlePosition = Math.max(MIN_THROTTLE_POSITION,
            Math.min(MAX_THROTTLE_POSITION, currentThrottle + throttleChange + rand.nextGaussian() * 2.0));

        final Instant prevTimestamp = this.prevRawData.timestamp().get();
        final int delayMillis = (int) (MIN_DELAY_MILLIS
            + this.rand.nextDouble() * (MAX_DELAY_MILLIS - MIN_DELAY_MILLIS));
        final Instant newTimestamp = prevTimestamp.plusMillis(delayMillis);

        final double speedMS = newRpm * TRANSMISSION_RATIO * WHEEL_RADIUS * 2 * Math.PI / 60.0;
        final int speedKMH = (int) (speedMS * 3.6);

        final RawData rawData = RawData.builder()
                .engineRPM(Optional.of(newRpm))
                .torque(Optional.of(newTorque))
                .engineTemperature(Optional.of(newTemperature))
                .throttlePosition(Optional.of(newThrottlePosition))
                .vehicleSpeed(Optional.of(speedKMH))
                .timestamp(Optional.of(newTimestamp))
                .build();

        this.prevRawData = rawData;
        return rawData;
    }

    @Override
    public DataSource getDynoType() {
        return DataSource.REAL_DYNO;
    }

    @Override
    public void begin() {
        this.isActive = true;
    }

    @Override
    public void end() {
        this.isActive = false;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }
}

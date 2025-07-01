package it.unibo.javadyno.model.data.impl;

import java.util.Objects;
import it.unibo.javadyno.model.data.api.DataElaborator;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.api.UserSettings;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Implementation of the Data Elaborator Component.
 * This class is responsible for processing raw data and producing elaborated data.
 */
public final class DataElaboratorImpl implements DataElaborator {

    private static final double HEARTH_GRAVITY_ACCELERATION = 9.81;
    private static final double KMH_TO_MS = 3.6;
    private static final double KW_TO_HP_MULTIPLIER = 1.3596;
    private static final int ENGINE_POWER_KW_DIVISOR = 9549;
    private static final double SPECIFIC_GAS_COSTANT_DRY_AIR = 287.05;
    private static final double ZERO_CELSIUS_IN_KELVIN = 273.15;
    private final UserSettings userSettings;
    private final Dyno dyno;
    private final DataSource dataSource;
    private RawData prevRawData;

    /**
     * Constructor for DataElaboratorImpl.
     *
     * @param dynoSource the source of the dynamometer data
     * @param userSettings the user settings to be used for data elaboration
     */
    public DataElaboratorImpl(final Dyno dynoSource, final UserSettings userSettings) {
        this.dyno = Objects.requireNonNull(dynoSource, "Dyno source cannot be null");
        this.dataSource = dyno.getDynoType();
        this.userSettings = Objects.requireNonNull(userSettings, "User settings cannot be null");
    }

    /**
     * Retrieves the RawData from the Dyno and processes it to produce an ElaboratedData object.
     *
     * @return an ElaboratedData object containing the processed data, or null if the data cannot be processed
     */
    @Override
    public ElaboratedData getElaboratedData() {
        final RawData rawData = this.dyno.getRawData();

        switch (this.dataSource) {
            case DataSource.OBD2:
                return processOBD2Data(rawData);

            case DataSource.SIMULATED_DYNO:

            case DataSource.REAL_DYNO:
                return processDynoData(rawData);
        }

        throw new IllegalStateException("Unknown data source: " + this.dataSource);
    }

    /**
     * Elaborates the raw data received from the OBD2 Dyno.
     *
     * @param rawData the raw data to be processed
     * @return an ElaboratedData object containing the processed data that can be null
     */
    private ElaboratedData processOBD2Data(final RawData rawData) {
        if (!rawData.vehicleSpeed().isPresent()
            || !rawData.engineRPM().isPresent()
            || !rawData.timestamp().isPresent()) {
            return null;
        } else if (Objects.isNull(prevRawData)) {
            prevRawData = rawData;
            return null;
        }
        final Double vehicleSpeedDelta = (rawData.vehicleSpeed().get() - prevRawData.vehicleSpeed().get()) / KMH_TO_MS;
        final Double timeDelta = (double) (rawData.timestamp().get().toEpochMilli()
            - prevRawData.timestamp().orElseThrow().toEpochMilli());
        if (vehicleSpeedDelta <= 0) {
            return null;
        }
        final Double airDensity;
        if (rawData.ambientAirTemperature().isPresent() && rawData.baroPressure().isPresent()) {
            final Double temperatureInKelvin = rawData.ambientAirTemperature().get() + ZERO_CELSIUS_IN_KELVIN;
            airDensity = rawData.baroPressure().get() * 1000
                / (SPECIFIC_GAS_COSTANT_DRY_AIR * temperatureInKelvin);
        } else {
            airDensity = userSettings.getAirDensity();
        }
        final Double acceleration = vehicleSpeedDelta / (timeDelta / 1000) / KMH_TO_MS;
        final Double inertialForce = acceleration * userSettings.getVehicleMass();

        final Double rollingResistanceForce = userSettings.getRollingResistanceCoefficient()
            * userSettings.getVehicleMass()
            * HEARTH_GRAVITY_ACCELERATION;

        final Double aerodynamicDragForce = 0.5
            * userSettings.getAirDragCoefficient()
            * userSettings.getFrontalArea()
            * airDensity
            * Math.pow(rawData.vehicleSpeed().get() / KMH_TO_MS, 2);

        final Double totalForce = inertialForce + rollingResistanceForce + aerodynamicDragForce;
        final Double enginePowerKW = totalForce * (rawData.vehicleSpeed().get() / KMH_TO_MS)
            / userSettings.getDriveTrainEfficiency();
        final Double enginePowerHP = enginePowerKW * KW_TO_HP_MULTIPLIER;
        final Double engineCorrectedTorque = enginePowerKW
            * ENGINE_POWER_KW_DIVISOR / rawData.engineRPM().orElseThrow();

        prevRawData = rawData;
        return new ElaboratedData(
            rawData,
            enginePowerKW,
            enginePowerHP,
            engineCorrectedTorque
        );
    }

    /**
     * Processes the raw data from the Bench Dyno.
     *
     * @param rawData the raw data to be processed
     * @return an ElaboratedData object containing the processed data that can be null
     */
    private ElaboratedData processDynoData(final RawData rawData) {
        if (!rawData.torque().isPresent() || !rawData.engineRPM().isPresent()) {
            return null;
        }
        final Double engineCorrectedTorque = rawData.torque().get()
            * userSettings.getLoadcellLeverLength();
        final Double enginePowerKW = engineCorrectedTorque * rawData.engineRPM().get() / ENGINE_POWER_KW_DIVISOR;
        final Double enginePowerHP = enginePowerKW * KW_TO_HP_MULTIPLIER;
        return new ElaboratedData(
            rawData,
            enginePowerKW,
            enginePowerHP,
            engineCorrectedTorque
        );
    }
}

package it.unibo.javadyno.model.data.impl;

import java.util.Objects;
import java.util.Optional;

import it.unibo.javadyno.controller.impl.AlertMonitor;
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

    private static final double KW_TO_HP_DIVISOR = 1.3596;
    private static final int ENGINE_POWER_KW_DIVISOR = 9549;
    private final Dyno dyno;
    private final DataSource dataSource;

    /**
     * Constructor for DataElaboratorImpl.
     *
     * @param dynoSource the source of the dynamometer data
     */
    public DataElaboratorImpl(final Dyno dynoSource) {
        this.dyno = Objects.requireNonNull(dynoSource, "Dyno source cannot be null");
        this.dataSource = dyno.getDynoType();
    }

    /**
     * {@inheritDoc}
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
     * Elaborates the raw data received from the DataTransreciever.
     *
     * @param rawData the raw data to be processed
     * @return an ElaboratedData object containing the processed data
     */
    private ElaboratedData processOBD2Data(final RawData rawData) {
        //TODO Implement the logic to elaborate the raw data
        return new ElaboratedData(
            rawData,
            0.0,
            0.0,
            0.0
        );
    }

    private ElaboratedData processDynoData(final RawData rawData) {
        if (!rawData.torque().isPresent() || !rawData.engineRPM().isPresent()) {
            AlertMonitor.warningNotify(
                "Raw data must contain both torque and RPM values",
                Optional.empty()
            );
            //throw new IllegalStateException("Raw data must contain both torque and RPM values.");
        }
        final Double engineCorrectedTorque =
            rawData.torque().get()
            * (Double) UserSettings.LOADCELL_LEVER_LENGTH.getDefaultValue();
        final Double enginePowerKW = engineCorrectedTorque * rawData.engineRPM().get() / ENGINE_POWER_KW_DIVISOR;
        final Double enginePowerHP = enginePowerKW * KW_TO_HP_DIVISOR;
        return new ElaboratedData(
            rawData,
            enginePowerKW,
            enginePowerHP,
            engineCorrectedTorque
        );
    }
}

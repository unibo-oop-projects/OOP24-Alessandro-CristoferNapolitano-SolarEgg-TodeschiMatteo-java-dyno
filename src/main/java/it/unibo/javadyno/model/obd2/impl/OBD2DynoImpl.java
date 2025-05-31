package it.unibo.javadyno.model.obd2.impl;

import java.util.Optional;

import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.obd2.api.OBD2Dyno;

/**
 * Implementation of the OBD2Dyno interface.
 * It retrieve engine RPM, vehicle speed, and other vehicle data from the CANBUS line
 * packed in a RawData object.
 */
public class OBD2DynoImpl implements OBD2Dyno {

    private Optional<Integer> engineRpm;
    private Optional<Integer> vehicleSpeed;
    private Optional<Double> engineTemperature;

    /**
     * {@inheritDoc}
     */
    @Override
    public RawData getRawData() {
        // Build and return a RawData object with the available data.
        throw new UnsupportedOperationException("Unimplemented method 'getRawData'");
    }
}

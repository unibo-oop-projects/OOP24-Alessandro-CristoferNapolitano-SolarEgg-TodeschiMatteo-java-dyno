package it.unibo.javadyno.model.dyno.obd2.impl;

import java.util.Optional;

import javax.sql.DataSource;

import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Implementation of the Dyno interface.
 * It retrieve engine RPM, vehicle speed, and other vehicle data from the OBD2 line
 * packed in a RawData object.
 */
public class OBD2Dyno implements Dyno {

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

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSource getDynoType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDynoType'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'begin'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'end'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isActive'");
    }
}

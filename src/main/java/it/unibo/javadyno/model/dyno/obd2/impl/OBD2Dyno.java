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
public class OBD2Dyno implements Dyno, Runnable {

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    /**
     * Factory method to create a new OBD2Dyno Virtual thread.
     * Requires Java 21 or --enable-preview option.
     *
     * @return a new Thread instance running the OBD2Dyno.
     */
    public static Thread make() {
        return Thread.ofVirtual().unstarted(new OBD2Dyno());
    }
}

package it.unibo.javadyno.model.data.impl;

import java.util.Objects;
import it.unibo.javadyno.model.data.api.DataElaborator;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Implementation of the Data Elaborator Component.
 * This class is responsible for processing raw data and producing elaborated data.
 */
public final class DataElaboratorImpl implements DataElaborator {
    
    private final Dyno dyno;
    private final DataSource dataSource;

    /**
     * Constructor for DataElaboratorImpl.
     *
     * @param dataTransreciever the data transreceiver to use
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
        
            default:
                throw new IllegalStateException("Unsupported Data Source" + this.dataSource.getActualName());
        }

    }

    /**
     * Elaborates the raw data received from the DataTransreciever.
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
        return new ElaboratedData(
            rawData,
            0.0,
            0.0,
            0.0
        );
    };
}

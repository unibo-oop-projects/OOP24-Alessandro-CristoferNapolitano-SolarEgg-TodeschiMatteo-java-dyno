package it.unibo.javadyno.model.data.impl;

import it.unibo.javadyno.model.data.api.DataElaborator;
import it.unibo.javadyno.model.data.api.DataTransreciever;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.RawData;

/**
 * Implementation of the Data Elaborator Component.
 * This class is responsible for processing raw data and producing elaborated data.
 */
public class DataElaboratorImpl implements DataElaborator {

    private ElaboratedData elaboratedData;
    private final DataTransreciever dataTransreciever;

    /**
     * Constructor for DataElaboratorImpl.
     *
     * @param dataTransreciever the data transreceiver to use
     */
    public DataElaboratorImpl(final DataTransreciever dataTransreciever) {
        this.dataTransreciever = dataTransreciever;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ElaboratedData getElaboratedData() {
        elaborateData(); // Ensure data is elaborated before returning
        return this.elaboratedData;
    }

    /**
     * Elaborates the raw data received from the DataTransreciever.
     */
    void elaborateData() {
        final RawData rawData = this.dataTransreciever.getRawData();
        //TODO Implement the logic to elaborate the raw data
        this.elaboratedData = new ElaboratedData(
            rawData,
            0.0,
            0.0,
            0.0
        );
    }
}

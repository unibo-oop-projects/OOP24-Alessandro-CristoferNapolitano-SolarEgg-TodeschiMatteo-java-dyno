package it.unibo.javadyno.model.data.impl;

import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.DataTransreciever;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Implementation of the Data Transreceiver Component.
 */
public class DataTransreceiverImpl implements DataTransreciever {

    private Dyno dyno;
    private DataSource dataSource;

    /**
     * {@inheritDoc}
     */
    @Override
    public RawData getRawData() {
        if (this.dyno == null || this.dataSource == null) {
            throw new IllegalStateException("DataTransreceiver not initialized with a DataSource and Dyno.");
        }
        return this.dyno.getRawData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(final Dyno dynoSource, final DataSource sourceType) {
        this.dyno = dynoSource;
        this.dataSource = sourceType;
    }
}

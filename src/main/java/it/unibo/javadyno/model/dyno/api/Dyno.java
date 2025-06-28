package it.unibo.javadyno.model.dyno.api;

import it.unibo.javadyno.model.data.api.DataSource;

import it.unibo.javadyno.model.data.api.RawData;

/**
 * Global Dyno Interface.
 */
public interface Dyno {
    /**
     * @return the data package containing the raw data.
     */
    RawData getRawData();

    /**
     * @return the type of dyno as a DataSource.
     */
    DataSource getDynoType();

    /**
     * Starts the dyno.
     * Data will be collectable after this method is called.
     */
    void begin();

    /**
     * Stops the dyno.
     */
    void end();

    /**
     * Checks if the dyno has been already started and can provide data.
     *
     * @return true if the dyno is active, false otherwise.
     */
    boolean isActive();
}

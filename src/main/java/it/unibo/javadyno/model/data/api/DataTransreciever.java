package it.unibo.javadyno.model.data.api;

import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * DataTransreciever interface.
 */
public interface DataTransreciever {
    /**
     * Retrieves the updated raw data from the data source.
     *
     * @return the latest raw data
     */
    RawData getRawData();

    /**
     * Sets the data source from which to retrieve the raw data.
     *
     * @param dataSource the data source type to set.
     * @param dyno the dyno instance to associate with the data source
     */
    void begin(Dyno dyno, DataSource dataSource);

}

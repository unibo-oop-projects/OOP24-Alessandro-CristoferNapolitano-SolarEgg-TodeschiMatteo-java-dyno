package it.unibo.javadyno.model.data.api;

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
     * @param dataSource the data source to set.
     */
    void begin(DataSource dataSource);

}

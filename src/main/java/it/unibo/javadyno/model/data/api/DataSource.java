package it.unibo.javadyno.model.data.api;

/**
 * Enum representing the different data sources available in the application.
 */
public enum DataSource {
    OBD2("OBD2"),
    SIMULATED_DYNO("Simulated Dyno"),
    REAL_DYNO("Real Dyno");

    private final String actualName;

    DataSource(final String actualName) {
        this.actualName = actualName;
    }

    /**
     * Retrieves the actual name of the data source.
     *
     * @return the actual name of the data source
     */
    public String getActualName() {
        return this.actualName;
    }
}

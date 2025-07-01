package it.unibo.javadyno.model.data.api;

import java.util.List;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Interface for collecting data.
 */
public interface DataCollector {

    /**
     * Clear all datas form Queue.
     *
     * @param dynoSource the source of the dynamometer data.
     * @param userSettings the user settings to be used for data collection.
     */
    void initialize(Dyno dynoSource, UserSettings userSettings);

    /**
     * Collects the latest data point.
     *
     * @return The collected elaborated data.
     */
    ElaboratedData collectData();

    /**
     * Gets all collected data.
     *
     * @return A list containing all collected data.
     */
    List<ElaboratedData> getFullData();
}

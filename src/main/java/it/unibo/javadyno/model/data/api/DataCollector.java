package it.unibo.javadyno.model.data.api;

import java.util.Queue;
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
     * @return a queue of all the datas collected.
     */
    Queue<ElaboratedData> getFullData();

    /**
     * Collect the last data collected.
     *
     * @return the last data collected.
     */
    ElaboratedData collectData();
}

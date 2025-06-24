package it.unibo.javadyno.model.data.api;

import java.util.Queue;

/**
 * Interface for collecting data.
 */
public interface DataCollector {

    /**
     * Clear all datas form Queue.
     */
    void clearData();

    /**
     * @return a queue of all the datas collected by simulation.
     */
    Queue<ElaboratedData> getFullData();

    /**
     * Collect the last data collected by simulation.
     */
    void collectData();
}

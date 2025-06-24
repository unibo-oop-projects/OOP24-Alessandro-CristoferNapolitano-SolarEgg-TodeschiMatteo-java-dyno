package it.unibo.javadyno.model.data.api;

import java.time.Instant;
import java.util.List;

/**
 * Interface for collecting data.
 */
public interface DataCollector {
    /**
     * @return a list of all the datas collected by simulation.
     */
    List<ElaboratedData> getFullData();

    /**
     * @param timestamp the time instant to retrieve data for.
     * @return data of a specified time instant by simulation.
     */
    ElaboratedData getInstantData(Instant timestamp);
}

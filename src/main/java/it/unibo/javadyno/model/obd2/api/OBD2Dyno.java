package it.unibo.javadyno.model.obd2.api;

import it.unibo.javadyno.model.data.api.RawData;

/**
 * OBD2Dyno interface.
 */
public interface OBD2Dyno {
    /**
     * Retrieves the istantaneous information available stored from the OBD2.
     *
     * @return a immutable snapshot of the stored data.
     */
    RawData getRawData();
}

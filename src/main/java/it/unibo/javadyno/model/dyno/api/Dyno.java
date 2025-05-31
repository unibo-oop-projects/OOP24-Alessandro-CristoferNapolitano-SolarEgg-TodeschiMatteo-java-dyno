package it.unibo.javadyno.model.dyno.api;

import it.unibo.javadyno.model.data.api.RawData;

/**
 * Global Dyno Interface.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface") // This interface is is not a functional interface
public interface Dyno {
    /**
     * @return the data package containing the raw data.
     */
    RawData getDatas();
}

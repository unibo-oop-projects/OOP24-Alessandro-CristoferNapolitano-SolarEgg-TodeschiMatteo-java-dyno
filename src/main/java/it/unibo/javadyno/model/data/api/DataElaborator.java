package it.unibo.javadyno.model.data.api;

/**
 * Interface for the Data Elaborator Component.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface DataElaborator {
    /**
     * Retrieves the elaborated data.
     *
     * @return the elaborated data record
     */
    ElaboratedData getElaboratedData();
}

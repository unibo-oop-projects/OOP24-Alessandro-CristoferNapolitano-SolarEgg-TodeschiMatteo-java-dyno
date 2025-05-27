package it.unibo.javadyno.model.obd2.api;

/**
 * OBD2Dyno interface.
 */
public interface OBD2Dyno {
    /**
     * Retrieves the engine RPM (Revolutions Per Minute).
     *
     * @return the engine RPM.
     */
    Integer getEngineRPM();

    /**
     * Retrieves the vehicle speed in Km/h.
     *
     * @return the vehicle speed.
     */
    Integer getSpeed();

    /**
     * Retrieves the engine coolant temperature.
     *
     * @return the engine temperature.
     */
    Double getEngineTemperature();
}

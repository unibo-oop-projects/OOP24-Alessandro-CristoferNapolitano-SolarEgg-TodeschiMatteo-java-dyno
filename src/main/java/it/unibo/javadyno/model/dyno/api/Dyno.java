package it.unibo.javadyno.model.dyno.api;

/**
 * Global Dyno Interface.
 */
public interface Dyno {
    /**
     * @return the engine's RPM
     */
    int getEngineRPM();

    /**
     * @return the roller's RPM
     */
    int getRollerRPM();

    /**
     * @return the engine's temperature
     */
    double getEngineTemp();

    /**
     * @return the value of the force produced by the engine
     */
    double getLoadCellValue();
}

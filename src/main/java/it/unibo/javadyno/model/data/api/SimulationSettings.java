package it.unibo.javadyno.model.data.api;

/**
 * SimulationSettings record that defines the settings for the simulation.
 *
 * @param updateTimeDelta the time delta for simulation updates in milliseconds.
 */
public record SimulationSettings(
    int updateTimeDelta
) {
}

package it.unibo.javadyno.model.data.api;

/**
 * Record representing settings for a simulated dyno.
 *
 * @param simulationUpdateTimeDelta the time delta for simulation updates in milliseconds
 */
public record RealDynoSettings(int simulationUpdateTimeDelta) {
}

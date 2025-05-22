package it.unibo.javadyno.model.data.api;

import java.time.Instant;
import java.util.Optional;

/**
 * RawData interface.
 * 
 * @param engineRPM the engine revolutions per minute
 * @param engineTemperature the engine coolant temperature
 * @param rollerRPM the roller revolutions per minute (Optional)
 * @param torque the torque measured by the dynamometer in Newton per meter [Nm] (Optional)
 * @param vehicleSpeed the vehicle speed in Km/h (Optional)
 * @param timestamp the timestamp (Optional)
 * @param throttlePosition the throttle position (Optional)
 * @param boostPressure the boost pressure (Optional)
 * @param exhaustGasTemperature the exhaust gas temperature (Optional)
 */
public record RawData(
    Integer engineRPM,
    Double engineTemperature,
    Optional<Integer> rollerRPM,
    Optional<Double> torque,
    Optional<Integer> vehicleSpeed,
    Optional<Instant> timestamp,
    Optional<Double> throttlePosition,
    Optional<Double> boostPressure,
    Optional<Double> exhaustGasTemperature
) {
}

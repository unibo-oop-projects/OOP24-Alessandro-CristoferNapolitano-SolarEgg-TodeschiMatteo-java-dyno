package it.unibo.javadyno.model.data.api;

import java.time.Instant;
import java.util.Optional;

/**
 * RawData interface.
 *
 * @param engineRPM the engine revolutions per minute
 * @param engineTemperature the engine coolant temperature
 * @param rollerRPM the roller revolutions per minute
 * @param torque the torque measured by the dynamometer in Newton per meter [Nm]
 * @param vehicleSpeed the vehicle speed in Km/h
 * @param timestamp the timestamp
 * @param throttlePosition the throttle position
 * @param baroPressure the boost pressure
 * @param ambientAirTemperature the ambient air pressure
 * @param exhaustGasTemperature the exhaust gas temperature
 */
public record RawData(
    Optional<Integer> engineRPM,
    Optional<Double> engineTemperature,
    Optional<Integer> rollerRPM,
    Optional<Double> torque,
    Optional<Integer> vehicleSpeed,
    Optional<Instant> timestamp,
    Optional<Double> throttlePosition,
    Optional<Integer> baroPressure,
    Optional<Integer> ambientAirTemperature,
    Optional<Double> exhaustGasTemperature
) {

    /**
     * Creates a new RawData instance.
     *
     * @param engineRPM the engine revolutions per minute
     * @param engineTemperature the engine coolant temperature
     * @param rollerRPM the roller revolutions per minute
     * @param torque the torque measured by the dynamometer in Newton per meter [Nm]
     * @param vehicleSpeed the vehicle speed in Km/h
     * @param timestamp the timestamp
     * @param throttlePosition the throttle position
     * @param baroPressure the boost pressure
     * @param ambientAirTemperature the ambient air pressure
     * @param exhaustGasTemperature the exhaust gas temperature
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for RawData.
     */
    public static final class Builder {
        private Optional<Integer> engineRPM = Optional.empty();
        private Optional<Double> engineTemperature = Optional.empty();
        private Optional<Integer> rollerRPM = Optional.empty();
        private Optional<Double> torque = Optional.empty();
        private Optional<Integer> vehicleSpeed = Optional.empty();
        private Optional<Instant> timestamp = Optional.empty();
        private Optional<Double> throttlePosition = Optional.empty();
        private Optional<Integer> baroPressure = Optional.empty();
        private Optional<Integer> ambientAirPressure = Optional.empty();
        private Optional<Double> exhaustGasTemperature = Optional.empty();

        /**
         * Sets the engine RPM.
         *
         * @param valueEngineRPM the engine revolutions per minute
         * @return this builder instance
         */
        public Builder engineRPM(final Optional<Integer> valueEngineRPM) {
            this.engineRPM = valueEngineRPM;
            return this;
        }

        /**
         * Sets the engine temperature.
         *
         * @param valueEngineTemperature the engine coolant temperature (Optional)
         * @return this builder instance
         */
        public Builder engineTemperature(final Optional<Double> valueEngineTemperature) {
            this.engineTemperature = valueEngineTemperature;
            return this;
        }

        /**
         * Sets the roller RPM.
         *
         * @param valueRollerRPM the roller revolutions per minute (Optional)
         * @return this builder instance
         */
        public Builder rollerRPM(final Optional<Integer> valueRollerRPM) {
            this.rollerRPM = valueRollerRPM;
            return this;
        }

        /**
         * Sets the torque.
         *
         * @param valueTorque the torque measured by the dynamometer in Newton per meter [Nm] (Optional)
         * @return this builder instance
         */
        public Builder torque(final Optional<Double> valueTorque) {
            this.torque = valueTorque;
            return this;
        }

        /**
         * Sets the vehicle speed.
         *
         * @param valueVehicleSpeed the vehicle speed in Km/h (Optional)
         * @return this builder instance
         */
        public Builder vehicleSpeed(final Optional<Integer> valueVehicleSpeed) {
            this.vehicleSpeed = valueVehicleSpeed;
            return this;
        }

        /**
         * Sets the timestamp to retrieve the moment this record was built.
         *
         * @param valueTimestamp the timestamp (Optional)
         * @return this builder instance
         */
        public Builder timestamp(final Optional<Instant> valueTimestamp) {
            this.timestamp = valueTimestamp;
            return this;
        }

        /**
         * Sets the throttle position value.
         *
         * @param valueThrottlePosition the throttle position (Optional)
         * @return this builder instance
         */
        public Builder throttlePosition(final Optional<Double> valueThrottlePosition) {
            this.throttlePosition = valueThrottlePosition;
            return this;
        }

        /**
         * Sets the boost pressure.
         *
         * @param valueBaroPressure the boost pressure (Optional)
         * @return this builder instance
         */
        public Builder baroPressure(final Optional<Integer> valueBaroPressure) {
            this.baroPressure = valueBaroPressure;
            return this;
        }

        /**
         * Sets the ambient air pressure.
         *
         * @param valueAmbientAirTemperature the ambient air pressure (Optional)
         * @return this builder instance
         */
        public Builder ambientAirTemperature(final Optional<Integer> valueAmbientAirTemperature) {
            this.ambientAirPressure = valueAmbientAirTemperature;
            return this;
        }

        /**
         * Sets the exhaust gas temperature.
         *
         * @param valueExhaustGasTemperature the exhaust gas temperature (Optional)
         * @return this builder instance
         */
        public Builder exhaustGasTemperature(final Optional<Double> valueExhaustGasTemperature) {
            this.exhaustGasTemperature = valueExhaustGasTemperature;
            return this;
        }

        /**
         * Builds a new RawData instance with the set parameters.
         *
         * @return a new RawData instance
         * @throws IllegalArgumentException if required fields are missing
         */
        public RawData build() {
            return new RawData(
                engineRPM,
                engineTemperature,
                rollerRPM,
                torque,
                vehicleSpeed,
                timestamp,
                throttlePosition,
                baroPressure,
                ambientAirPressure,
                exhaustGasTemperature
            );
        }
    }
}

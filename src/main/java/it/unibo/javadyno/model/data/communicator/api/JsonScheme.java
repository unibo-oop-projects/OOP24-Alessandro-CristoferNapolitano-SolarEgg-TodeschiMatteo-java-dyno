package it.unibo.javadyno.model.data.communicator.api;

/**
 * Enum representing the JSON data scheme used by the MCU.
 */
public enum JsonScheme {
    ENGINE_RPM("engineRPM"),
    VEHICLE_SPEED("vehicleSpeed"),
    ENGINE_TEMPERATURE("engineTemperature"),
    TORQUE("torque"),
    THROTTLE_POSITION("throttlePosition"),
    TIMESTAMP("timestamp");

    private final String actualName;

    JsonScheme(final String actualName) {
        this.actualName = actualName;
    }

    /**
     * Retrieves the actual name of the JSON element.
     *
     * @return the actual name of the JSON scheme
     */
    public String getActualName() {
        return this.actualName;
    }
}

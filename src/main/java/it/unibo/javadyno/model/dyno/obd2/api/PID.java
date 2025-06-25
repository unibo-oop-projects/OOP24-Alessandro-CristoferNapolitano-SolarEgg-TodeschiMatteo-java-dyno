package it.unibo.javadyno.model.dyno.obd2.api;

/**
 * Represents OBD2 Parameter IDs (PIDs).
 */
public enum PID {

    ENGINE_RPM(0x0C, "Engine RPM"),
    VEHICLE_SPEED(0x0D, "Vehicle Speed"),
    THROTTLE_POSITION(0x11, "Throttle Position"),
    ENGINE_COOLANT_TEMPERATURE(0x05, "Engine Coolant Temperature", -40),
    MAF_AIR_FLOW_RATE(0x10, "MAF Air Flow Rate"),
    FUEL_LEVEL_INPUT(0x2F, "Fuel Level Input");

    private final int code;
    private final String description;
    private final int offset;

    PID(final int code, final String description, final int offset) {
        this.code = code;
        this.description = description;
        this.offset = offset;
    }

    PID(final int code, final String description) {
        this(code, description, 0); // Default offset to 0
    }

    /**
     * Retrieves the OBD-II PID code.
     *
     * @return the hexadecimal PID code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Retrieves the description of the PID.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Retrieves the offset for the PID value.
     *
     * @return the offset
     */
    public int getOffset() {
        return this.offset;
    }
}

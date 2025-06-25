package it.unibo.javadyno.model.dyno.obd2.api;

/**
 * OBD2 diagnostic modes as defined by SAE J1979.
 */
public enum Mode {

    CURRENT_DATA(0x01, "Show current data"),
    FREEZE_FRAME(0x02, "Show freeze frame data"),
    STORED_DTC(0x03, "Show stored Diagnostic Trouble Codes"),
    CLEAR_DTC(0x04, "Clear Diagnostic Trouble Codes and stored values"),
    TEST_RESULTS_O2(0x05, "Test results, oxygen sensor monitoring"),
    TEST_RESULTS_NONCONTIOUS(0x06, "Test results, other component/system monitoring"),
    PENDING_DTC(0x07, "Show pending Diagnostic Trouble Codes"),
    CONTROL_OPERATION(0x08, "Control operation of on-board component/system"),
    VEHICLE_INFO(0x09, "Request vehicle information"),
    PERMANENT_DTC(0x0A, "Permanent Diagnostic Trouble Codes");

    private final int code;
    private final String description;

    Mode(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Retrieves the OBD-II mode code.
     *
     * @return the hexadecimal mode code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Retrieves the description of the mode.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }
}

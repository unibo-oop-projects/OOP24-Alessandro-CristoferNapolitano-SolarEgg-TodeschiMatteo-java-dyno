package it.unibo.javadyno.model.data.api;

/**
 * Enum representing user settings with default values.
 */
public enum UserSettings {
    SIMULATION_UPDATE_TIME_DELTA(100),
    LOADCELL_LEVER_LENGTH(0.5),
    VEHICLE_MASS(1500),
    ROLLING_RESISTANCE_COEFFICIENT(0.012),
    AIR_DRAG_COEFFICIENT(0.32),
    FRONTAL_AREA(2.2),
    AIR_DENSITY(1.225),
    DRIVE_TRAIN_EFFICIENCY(0.85);

    private final Object defaultValue;

    /**
     * Constructor for UserSettings enum.
     *
     * @param defaultValue the default value for the setting
     */
    UserSettings(final Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Retrieves the default value of the setting.
     *
     * @return the default value
     */
    public Object getDefaultValue() {
        return this.defaultValue;
    }
}

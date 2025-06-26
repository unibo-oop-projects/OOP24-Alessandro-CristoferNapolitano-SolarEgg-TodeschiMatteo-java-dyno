package it.unibo.javadyno.model.data.api;

/**
 * Enum representing user settings with default values.
 */
public enum UserSettings {
    SIMULATION_UPDATE_TIME_DELTA(100),
    LOADCELL_LEVER_LENGTH(0.5);

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

package it.unibo.javadyno.model.data.api;

/**
 * Represents user setting definitions with default values.
 */
public enum UserSettingDef {

    SIMULATION_UPDATE_TIME_DELTA(100.0),
    LOADCELL_LEVER_LENGTH(0.5),
    VEHICLE_MASS(1500.0),
    ROLLING_RESISTANCE_COEFFICIENT(0.012),
    AIR_DRAG_COEFFICIENT(0.32),
    FRONTAL_AREA(2.2),
    AIR_DENSITY(1.225),
    DRIVE_TRAIN_EFFICIENCY(0.85),
    DYNO_TYPE(DataSource.REAL_DYNO.ordinal());

    private final double defaultValue;

    UserSettingDef(final double defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Retrieves the default value for this setting.
     *
     * @return the default value
     */
    public double getDefaultValue() {
        return this.defaultValue;
    }
}

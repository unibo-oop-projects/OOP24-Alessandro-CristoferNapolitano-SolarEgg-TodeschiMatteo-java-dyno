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
    DYNO_TYPE(DataSource.REAL_DYNO.ordinal()),

    BASE_TORQUE(0.0),
    TORQUE_PER_RAD(0.0),
    ENGINE_INERTIA(0.0),
    GEAR_RATIOS(1.0),
    WHEEL_MASS(10.0),
    WHEEL_RADIUS(0.3),
    ROLLING_COEFF(0.012);


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

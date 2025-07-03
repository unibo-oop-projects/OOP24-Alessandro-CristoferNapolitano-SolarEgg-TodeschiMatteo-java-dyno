package it.unibo.javadyno.model.data.api;

/**
 * Represents user setting definitions with default values.
 */
public enum UserSettingDef {

    SIMULATION_UPDATE_TIME_DELTA(100.0),
    SIMULATION_MAX_RPM(8000),
    LOADCELL_LEVER_LENGTH(0.5),
    VEHICLE_MASS(1500.0),
    ROLLING_RESISTANCE_COEFFICIENT(0.012),
    AIR_DRAG_COEFFICIENT(0.32),
    FRONTAL_AREA(2.2),
    AIR_DENSITY(1.225),
    DRIVE_TRAIN_EFFICIENCY(0.85),
    DYNO_TYPE(DataSource.OBD2.ordinal()),
    ROLLING_COEFF(0.012),

    BASE_TORQUE(120.0),
    TORQUE_PER_RAD(0.05),
    ENGINE_INERTIA(0.4),
    GEAR_RATIOS(1.3),
    WHEEL_MASS(20.0),
    WHEEL_RADIUS(0.3),
    AIR_TEMPERATURE(20.0),
    AIR_PRESSURE(101.3),
    AIR_HUMIDITY(50.0);

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

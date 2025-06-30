package it.unibo.javadyno.model.data.api;

/**
 * Static class representing user settings with default values.
 */
public final class UserSettings {

    private static double simulationUpdateTimeDelta = UserSettingDef.SIMULATION_UPDATE_TIME_DELTA.getDefaultValue();
    private static double loadcellLeverLength = UserSettingDef.LOADCELL_LEVER_LENGTH.getDefaultValue();
    private static double vehicleMass = UserSettingDef.VEHICLE_MASS.getDefaultValue();
    private static double rollingResistanceCoefficient = UserSettingDef.ROLLING_RESISTANCE_COEFFICIENT.getDefaultValue();
    private static double airDragCoefficient = UserSettingDef.AIR_DRAG_COEFFICIENT.getDefaultValue();
    private static double frontalArea = UserSettingDef.FRONTAL_AREA.getDefaultValue();
    private static double airDensity = UserSettingDef.AIR_DENSITY.getDefaultValue();
    private static double driveTrainEfficiency = UserSettingDef.DRIVE_TRAIN_EFFICIENCY.getDefaultValue();
    private static double dynoType = UserSettingDef.DYNO_TYPE.getDefaultValue();

    /**
     * Private constructor to prevent instantiation.
     */
    private UserSettings() {
        // Utility class
    }

    /**
     * Gets the simulation update time delta.
     *
     * @return the simulation update time delta in milliseconds
     */
    public static double getSimulationUpdateTimeDelta() {
        return simulationUpdateTimeDelta;
    }

    /**
     * Gets the loadcell lever length.
     *
     * @return the loadcell lever length in meters
     */
    public static double getLoadcellLeverLength() {
        return loadcellLeverLength;
    }

    /**
     * Gets the vehicle mass.
     *
     * @return the vehicle mass in kilograms
     */
    public static double getVehicleMass() {
        return vehicleMass;
    }

    /**
     * Gets the rolling resistance coefficient.
     *
     * @return the rolling resistance coefficient
     */
    public static double getRollingResistanceCoefficient() {
        return rollingResistanceCoefficient;
    }

    /**
     * Gets the air drag coefficient.
     *
     * @return the air drag coefficient
     */
    public static double getAirDragCoefficient() {
        return airDragCoefficient;
    }

    /**
     * Gets the frontal area.
     *
     * @return the frontal area in square meters
     */
    public static double getFrontalArea() {
        return frontalArea;
    }

    /**
     * Gets the air density.
     *
     * @return the air density in kg/m³
     */
    public static double getAirDensity() {
        return airDensity;
    }

    /**
     * Gets the drive train efficiency.
     *
     * @return the drive train efficiency as a decimal (0 - 1.0)
     */
    public static double getDriveTrainEfficiency() {
        return driveTrainEfficiency;
    }

    /**
     * Gets the dyno type.
     *
     * @return the dyno type
     */
    public static DataSource getDynoType() {
        return DataSource.values()[(int) dynoType];
    }

    /**
     * Sets the simulation update time delta.
     *
     * @param simulationUpdateTimeDelta the simulation update time delta in milliseconds
     */
    public static void setSimulationUpdateTimeDelta(final double simulationUpdateTimeDelta) {
        UserSettings.simulationUpdateTimeDelta = simulationUpdateTimeDelta;
    }

    /**
     * Sets the loadcell lever length.
     *
     * @param loadcellLeverLength the loadcell lever length in meters
     */
    public static void setLoadcellLeverLength(final double loadcellLeverLength) {
        UserSettings.loadcellLeverLength = loadcellLeverLength;
    }

    /**
     * Sets the vehicle mass.
     *
     * @param vehicleMass the vehicle mass in kilograms
     */
    public static void setVehicleMass(final double vehicleMass) {
        UserSettings.vehicleMass = vehicleMass;
    }

    /**
     * Sets the rolling resistance coefficient.
     *
     * @param rollingResistanceCoefficient the rolling resistance coefficient
     */
    public static void setRollingResistanceCoefficient(final double rollingResistanceCoefficient) {
        UserSettings.rollingResistanceCoefficient = rollingResistanceCoefficient;
    }

    /**
     * Sets the air drag coefficient.
     *
     * @param airDragCoefficient the air drag coefficient
     */
    public static void setAirDragCoefficient(final double airDragCoefficient) {
        UserSettings.airDragCoefficient = airDragCoefficient;
    }

    /**
     * Sets the frontal area.
     *
     * @param frontalArea the frontal area in square meters
     */
    public static void setFrontalArea(final double frontalArea) {
        UserSettings.frontalArea = frontalArea;
    }

    /**
     * Sets the air density.
     *
     * @param airDensity the air density in kg/m³
     */
    public static void setAirDensity(final double airDensity) {
        UserSettings.airDensity = airDensity;
    }

    /**
     * Sets the drive train efficiency.
     *
     * @param driveTrainEfficiency the drive train efficiency as a decimal (0-1)
     */
    public static void setDriveTrainEfficiency(final double driveTrainEfficiency) {
        UserSettings.driveTrainEfficiency = driveTrainEfficiency;
    }

    /**
     * Sets the dyno type.
     *
     * @param dynoType the dyno type
     */
    public static void setDynoType(final double dynoType) {
        UserSettings.dynoType = dynoType;
    }

    /**
     * Resets all settings to their default values.
     */
    public static void resetToDefaults() {
        simulationUpdateTimeDelta = UserSettingDef.SIMULATION_UPDATE_TIME_DELTA.getDefaultValue();
        loadcellLeverLength = UserSettingDef.LOADCELL_LEVER_LENGTH.getDefaultValue();
        vehicleMass = UserSettingDef.VEHICLE_MASS.getDefaultValue();
        rollingResistanceCoefficient = UserSettingDef.ROLLING_RESISTANCE_COEFFICIENT.getDefaultValue();
        airDragCoefficient = UserSettingDef.AIR_DRAG_COEFFICIENT.getDefaultValue();
        frontalArea = UserSettingDef.FRONTAL_AREA.getDefaultValue();
        airDensity = UserSettingDef.AIR_DENSITY.getDefaultValue();
        driveTrainEfficiency = UserSettingDef.DRIVE_TRAIN_EFFICIENCY.getDefaultValue();
        dynoType = UserSettingDef.DYNO_TYPE.getDefaultValue();
    }

}

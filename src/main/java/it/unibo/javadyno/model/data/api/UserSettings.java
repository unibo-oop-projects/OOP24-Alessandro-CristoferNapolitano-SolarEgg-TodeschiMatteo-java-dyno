package it.unibo.javadyno.model.data.api;

/**
 * Static class representing user settings with default values.
 */
public final class UserSettings {
    
    // Default values
    private static final double DEFAULT_SIMULATION_UPDATE_TIME_DELTA = 100.0;
    private static final double DEFAULT_LOADCELL_LEVER_LENGTH = 0.5;
    private static final double DEFAULT_VEHICLE_MASS = 1500.0;
    private static final double DEFAULT_ROLLING_RESISTANCE_COEFFICIENT = 0.012;
    private static final double DEFAULT_AIR_DRAG_COEFFICIENT = 0.32;
    private static final double DEFAULT_FRONTAL_AREA = 2.2;
    private static final double DEFAULT_AIR_DENSITY = 1.225;
    private static final double DEFAULT_DRIVE_TRAIN_EFFICIENCY = 0.85;

    // Current values
    private static double simulationUpdateTimeDelta = DEFAULT_SIMULATION_UPDATE_TIME_DELTA;
    private static double loadcellLeverLength = DEFAULT_LOADCELL_LEVER_LENGTH;
    private static double vehicleMass = DEFAULT_VEHICLE_MASS;
    private static double rollingResistanceCoefficient = DEFAULT_ROLLING_RESISTANCE_COEFFICIENT;
    private static double airDragCoefficient = DEFAULT_AIR_DRAG_COEFFICIENT;
    private static double frontalArea = DEFAULT_FRONTAL_AREA;
    private static double airDensity = DEFAULT_AIR_DENSITY;
    private static double driveTrainEfficiency = DEFAULT_DRIVE_TRAIN_EFFICIENCY;
    
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
     * Resets all settings to their default values.
     */
    public static void resetToDefaults() {
        simulationUpdateTimeDelta = DEFAULT_SIMULATION_UPDATE_TIME_DELTA;
        loadcellLeverLength = DEFAULT_LOADCELL_LEVER_LENGTH;
        vehicleMass = DEFAULT_VEHICLE_MASS;
        rollingResistanceCoefficient = DEFAULT_ROLLING_RESISTANCE_COEFFICIENT;
        airDragCoefficient = DEFAULT_AIR_DRAG_COEFFICIENT;
        frontalArea = DEFAULT_FRONTAL_AREA;
        airDensity = DEFAULT_AIR_DENSITY;
        driveTrainEfficiency = DEFAULT_DRIVE_TRAIN_EFFICIENCY;
    }

}

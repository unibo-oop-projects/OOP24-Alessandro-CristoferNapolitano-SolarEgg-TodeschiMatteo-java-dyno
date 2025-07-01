package it.unibo.javadyno.model.data.api;

import java.io.Serializable;

/**
 * Class representing user settings with default values.
 */
public final class UserSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    private double simulationUpdateTimeDelta;
    private double loadcellLeverLength;
    private double vehicleMass;
    private double rollingResistanceCoefficient;
    private double airDragCoefficient;
    private double frontalArea;
    private double airDensity;
    private double driveTrainEfficiency;
    private double dynoType;

    private double baseTorque;
    private double torquePerRad;
    private double engineInertia;
    private double[] gearRatios;
    private double wheelMass;
    private double wheelRadius;
    private double rollingCoeff;
    private double airTemperature;
    private double airPressure;
    private double airHumidity;

    /**
     * Private constructor to prevent instantiation.
     */
    public UserSettings() {
        simulationUpdateTimeDelta = UserSettingDef.SIMULATION_UPDATE_TIME_DELTA.getDefaultValue();
        loadcellLeverLength = UserSettingDef.LOADCELL_LEVER_LENGTH.getDefaultValue();
        vehicleMass = UserSettingDef.VEHICLE_MASS.getDefaultValue();
        rollingResistanceCoefficient = UserSettingDef.ROLLING_RESISTANCE_COEFFICIENT.getDefaultValue();
        airDragCoefficient = UserSettingDef.AIR_DRAG_COEFFICIENT.getDefaultValue();
        frontalArea = UserSettingDef.FRONTAL_AREA.getDefaultValue();
        airDensity = UserSettingDef.AIR_DENSITY.getDefaultValue();
        driveTrainEfficiency = UserSettingDef.DRIVE_TRAIN_EFFICIENCY.getDefaultValue();
        dynoType = UserSettingDef.DYNO_TYPE.getDefaultValue();

        baseTorque = UserSettingDef.BASE_TORQUE.getDefaultValue();
        torquePerRad = UserSettingDef.TORQUE_PER_RAD.getDefaultValue();
        engineInertia = UserSettingDef.ENGINE_INERTIA.getDefaultValue();
        gearRatios = new double[]{UserSettingDef.GEAR_RATIOS.getDefaultValue()};
        wheelMass = UserSettingDef.WHEEL_MASS.getDefaultValue();
        wheelRadius = UserSettingDef.WHEEL_RADIUS.getDefaultValue();
        rollingCoeff = UserSettingDef.ROLLING_COEFF.getDefaultValue();
    }

    /**
     * Gets the simulation update time delta.
     *
     * @return the simulation update time delta in milliseconds
     */
    public double getSimulationUpdateTimeDelta() {
        return simulationUpdateTimeDelta;
    }

    /**
     * Gets the loadcell lever length.
     *
     * @return the loadcell lever length in meters
     */
    public double getLoadcellLeverLength() {
        return loadcellLeverLength;
    }

    /**
     * Gets the vehicle mass.
     *
     * @return the vehicle mass in kilograms
     */
    public double getVehicleMass() {
        return vehicleMass;
    }

    /**
     * Gets the rolling resistance coefficient.
     *
     * @return the rolling resistance coefficient
     */
    public double getRollingResistanceCoefficient() {
        return rollingResistanceCoefficient;
    }

    /**
     * Gets the air drag coefficient.
     *
     * @return the air drag coefficient
     */
    public double getAirDragCoefficient() {
        return airDragCoefficient;
    }

    /**
     * Gets the frontal area.
     *
     * @return the frontal area in square meters
     */
    public double getFrontalArea() {
        return frontalArea;
    }

    /**
     * Gets the air density.
     *
     * @return the air density in kg/m³
     */
    public double getAirDensity() {
        return airDensity;
    }

    /**
     * Gets the drive train efficiency.
     *
     * @return the drive train efficiency as a decimal (0 - 1.0)
     */
    public double getDriveTrainEfficiency() {
        return driveTrainEfficiency;
    }

    /**
     * Gets the dyno type.
     *
     * @return the dyno type
     */
    public DataSource getDynoType() {
        return DataSource.values()[(int) dynoType];
    }

    /**
     * Sets the simulation update time delta.
     *
     * @param simulationUpdateTimeDelta the simulation update time delta in milliseconds
     */
    public void setSimulationUpdateTimeDelta(final double simulationUpdateTimeDelta) {
        this.simulationUpdateTimeDelta = simulationUpdateTimeDelta;
    }

    /**
     * Sets the loadcell lever length.
     *
     * @param loadcellLeverLength the loadcell lever length in meters
     */
    public void setLoadcellLeverLength(final double loadcellLeverLength) {
        this.loadcellLeverLength = loadcellLeverLength;
    }

    /**
     * Sets the vehicle mass.
     *
     * @param vehicleMass the vehicle mass in kilograms
     */
    public void setVehicleMass(final double vehicleMass) {
        this.vehicleMass = vehicleMass;
    }

    /**
     * Sets the rolling resistance coefficient.
     *
     * @param rollingResistanceCoefficient the rolling resistance coefficient
     */
    public void setRollingResistanceCoefficient(final double rollingResistanceCoefficient) {
        this.rollingResistanceCoefficient = rollingResistanceCoefficient;
    }

    /**
     * Sets the air drag coefficient.
     *
     * @param airDragCoefficient the air drag coefficient
     */
    public void setAirDragCoefficient(final double airDragCoefficient) {
        this.airDragCoefficient = airDragCoefficient;
    }

    /**
     * Sets the frontal area.
     *
     * @param frontalArea the frontal area in square meters
     */
    public void setFrontalArea(final double frontalArea) {
        this.frontalArea = frontalArea;
    }

    /**
     * Sets the air density.
     *
     * @param airDensity the air density in kg/m³
     */
    public void setAirDensity(final double airDensity) {
        this.airDensity = airDensity;
    }

    /**
     * Sets the drive train efficiency.
     *
     * @param driveTrainEfficiency the drive train efficiency as a decimal (0-1)
     */
    public void setDriveTrainEfficiency(final double driveTrainEfficiency) {
        this.driveTrainEfficiency = driveTrainEfficiency;
    }

    /**
     * Sets the dyno type.
     *
     * @param dynoType the dyno type
     */
    public void setDynoType(final double dynoType) {
        this.dynoType = dynoType;
    }

    /**
     * Gets the base torque of the engine.
     *
     * @return the base torque
     */
    public double getBaseTorque() {
        return baseTorque;
    }

    /**
     * Sets the base torque of the engine.
     *
     * @param baseTorque the base torque
     */
    public void setBaseTorque(final double baseTorque) {
        this.baseTorque = baseTorque;
    }

    /**
     * Gets the torque per radian of the engine.
     *
     * @return the torque per radian
     */
    public double getTorquePerRad() {
        return torquePerRad;
    }

    /**
     * Sets the torque per radian of the engine.
     *
     * @param torquePerRad the torque per radian
     */
    public void setTorquePerRad(final double torquePerRad) {
        this.torquePerRad = torquePerRad;
    }

    /**
     * Gets the engine inertia.
     *
     * @return the engine inertia
     */
    public double getEngineInertia() {
        return engineInertia;
    }

    /**
     * Sets the engine inertia.
     *
     * @param engineInertia the engine inertia
     */
    public void setEngineInertia(final double engineInertia) {
        this.engineInertia = engineInertia;
    }

    /**
     * Gets the gear ratios of the vehicle.
     *
     * @return an array of gear ratios
     */
    public double[] getGearRatios() {
        return gearRatios.clone();
    }

    /**
     * Sets the gear ratios of the vehicle.
     *
     * @param gearRatios an array of gear ratios
     */
    public void setGearRatios(final double[] gearRatios) {
        this.gearRatios = gearRatios.clone();
    }

    /**
     * Gets the mass of the wheels.
     *
     * @return the mass of the wheels
     */
    public double getWheelMass() {
        return wheelMass;
    }

    /**
     * Sets the mass of the wheels.
     *
     * @param wheelMass the mass of the wheels
     */
    public void setWheelMass(final double wheelMass) {
        this.wheelMass = wheelMass;
    }

    /**
     * Gets the radius of the wheels.
     *
     * @return the radius of the wheels
     */
    public double getWheelRadius() {
        return wheelRadius;
    }

    /**
     * Sets the radius of the wheels.
     *
     * @param wheelRadius the radius of the wheels
     */
    public void setWheelRadius(final double wheelRadius) {
        this.wheelRadius = wheelRadius;
    }

    /**
     * Gets the rolling coefficient.
     *
     * @return the rolling coefficient
     */
    public double getRollingCoeff() {
        return rollingCoeff;
    }

    /**
     * Sets the rolling coefficient.
     *
     * @param rollingCoeff the rolling coefficient
     */
    public void setRollingCoeff(final double rollingCoeff) {
        this.rollingCoeff = rollingCoeff;
    }

    /**
     * Gets the air temperature.
     *
     * @return the air temperature in Celsius
     */
    public double getAirTemperature() {
        return airTemperature;
    }

    /**
     * Sets the air temperature.
     *
     * @param airTemperature the air temperature in Celsius
     */
    public void setAirTemperature(final double airTemperature) {
        this.airTemperature = airTemperature;
    }

    /**
     * Gets the air pressure.
     *
     * @return airPressure the air pressure in hPa
     */
    public double getAirPressure() {
        return airPressure;
    }

    /**
     * Sets the air pressure.
     *
     * @param airPressure the air pressure in hPa
     */
    public void setAirPressure(final double airPressure) {
        this.airPressure = airPressure;
    }

    /**
     * Gets the air humidity.
     *
     * @return airHumidity the air humidity in percentage
     */
    public double getAirHumidity() {
        return airHumidity;
    }

    /**
     * Sets the air humidity.
     *
     * @param airHumidity the air humidity in percentage
     */
    public void setAirHumidity(final double airHumidity) {
        this.airHumidity = airHumidity;
    }

    /**
     * Creates a copy of the current UserSettings instance.
     *
     * @return a new UserSettings instance with the same values
     */
    public UserSettings copy() {
        final UserSettings copy = new UserSettings();
        copy.simulationUpdateTimeDelta = this.simulationUpdateTimeDelta;
        copy.loadcellLeverLength = this.loadcellLeverLength;
        copy.vehicleMass = this.vehicleMass;
        copy.rollingResistanceCoefficient = this.rollingResistanceCoefficient;
        copy.airDragCoefficient = this.airDragCoefficient;
        copy.frontalArea = this.frontalArea;
        copy.airDensity = this.airDensity;
        copy.driveTrainEfficiency = this.driveTrainEfficiency;
        copy.dynoType = this.dynoType;

        copy.baseTorque = this.baseTorque;
        copy.torquePerRad = this.torquePerRad;
        copy.engineInertia = this.engineInertia;
        copy.gearRatios = this.gearRatios.clone();
        copy.wheelMass = this.wheelMass;
        copy.wheelRadius = this.wheelRadius;
        copy.rollingCoeff = this.rollingCoeff;
        copy.airTemperature = this.airTemperature;
        copy.airPressure = this.airPressure;
        copy.airHumidity = this.airHumidity;
        return copy;
    }

    /**
     * Resets all settings to their default values.
     */
    public void resetToDefaults() {
        simulationUpdateTimeDelta = UserSettingDef.SIMULATION_UPDATE_TIME_DELTA.getDefaultValue();
        loadcellLeverLength = UserSettingDef.LOADCELL_LEVER_LENGTH.getDefaultValue();
        vehicleMass = UserSettingDef.VEHICLE_MASS.getDefaultValue();
        rollingResistanceCoefficient = UserSettingDef.ROLLING_RESISTANCE_COEFFICIENT.getDefaultValue();
        airDragCoefficient = UserSettingDef.AIR_DRAG_COEFFICIENT.getDefaultValue();
        frontalArea = UserSettingDef.FRONTAL_AREA.getDefaultValue();
        airDensity = UserSettingDef.AIR_DENSITY.getDefaultValue();
        driveTrainEfficiency = UserSettingDef.DRIVE_TRAIN_EFFICIENCY.getDefaultValue();
        dynoType = UserSettingDef.DYNO_TYPE.getDefaultValue();

        baseTorque = UserSettingDef.BASE_TORQUE.getDefaultValue();
        torquePerRad = UserSettingDef.TORQUE_PER_RAD.getDefaultValue();
        engineInertia = UserSettingDef.ENGINE_INERTIA.getDefaultValue();
        gearRatios = new double[]{UserSettingDef.GEAR_RATIOS.getDefaultValue()};
        wheelMass = UserSettingDef.WHEEL_MASS.getDefaultValue();
        wheelRadius = UserSettingDef.WHEEL_RADIUS.getDefaultValue();
        rollingCoeff = UserSettingDef.ROLLING_COEFF.getDefaultValue();
        airTemperature = UserSettingDef.AIR_TEMPERATURE.getDefaultValue();
        airPressure = UserSettingDef.AIR_PRESSURE.getDefaultValue();
        airHumidity = UserSettingDef.AIR_HUMIDITY.getDefaultValue();
    }
}

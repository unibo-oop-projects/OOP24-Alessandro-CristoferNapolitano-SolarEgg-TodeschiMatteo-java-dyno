package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unibo.javadyno.model.dyno.simulated.api.BrakeTorqueProvider;
import it.unibo.javadyno.model.dyno.simulated.api.DriveTrain;
import it.unibo.javadyno.model.dyno.simulated.api.Engine;
import it.unibo.javadyno.model.dyno.simulated.api.LoadModel;
import it.unibo.javadyno.model.dyno.simulated.api.TemperatureModel;
import it.unibo.javadyno.model.dyno.simulated.api.Transmission;
import it.unibo.javadyno.model.dyno.simulated.api.WeatherStation;

/**
 * Vehicle Builder wich enables to build a simulation of a vehicle
 * Use static method ".builder" to start building the model
 * add call to ".with..." method to initialize parameters of the full build
 * at the end of calling method to inizialize parameters for the model call methods ".build...".
 */
public final class VehicleBuilder {
    // --- powertrain parameters ---
    /** base torque [Nm]. */
    private Double baseTorque;
    /** torque increase per rad/s [Nm/(rad/s)]. */
    private Double torquePerRad;
    /** engine rotational inertia [kg*m^2]. */
    private Double engineInertia;
    /** transmission gear ratios. */
    private double[] gearRatio;
    // --- thermal model parameters (with defaults) ---
    /** thermal capacity [J/°C]. */
    private double thermalCapacity;
    /** cooling coefficient [W/°C]. */
    private double coolingCoeff;
    /** custom temperature model, if injected. */
    private TemperatureModel temperatureModel;
    // --- wheel parameters ---
    /** wheel mass [kg]. */
    private Double wheelMass;
    /** wheel radius [m]. */
    private Double wheelRadius;

    // --- rolling resistance ---
    private boolean enableRollingResistance;
    /** rolling resistance coefficient [Nm/(rad/s)]. */
    private Double rollingCoeff;

    // --- bench brake torque ---
    private BrakeTorqueProvider benchBrakeTorqueProvider;

    // --- simulation enviroment status---
    private WeatherStation weatherStation;

    private VehicleBuilder() { }

    /**
     * starting method to build a vehicle implementation.
     *
     * @return VehicleBuilder
     */
    public static VehicleBuilder builder() {
        return new VehicleBuilder();
    }

    /**
     * VehicleBuilder with Base Torque for SimpleTorqueMap.
     *
     * @param baseTorqueValue base torque [Nm]
     * @return VehicleBuider
     */
    public VehicleBuilder withBaseTorque(final double baseTorqueValue) {
        this.baseTorque = baseTorqueValue;
        return this;
    }

    /**
     * VehicleBuilder with Torque per Radiant for SimpleTorqueMap.
     *
     * @param torquePerRadValue torque increase per rad/s [Nm/(rad/s)]
     * @return VehicleBuilder
     */
    public VehicleBuilder withTorquePerRad(final double torquePerRadValue) {
        this.torquePerRad = torquePerRadValue;
        return this;
    }

    /**
     * VehicleBuilder with Engine Inertia.
     *
     * @param engineInertiaValue engine rotational inertia [kg*m^2]
     * @return VehicleBuilder
     */
    public VehicleBuilder withEngineInertia(final double engineInertiaValue) {
        this.engineInertia = engineInertiaValue;
        return this;
    }

    /**
     * VehicleBuilder with a single or multiple gear ratios.
     *
     * @param gearRatioValue gear ratio
     * @return VehicleBuilder
     */
    public VehicleBuilder withGearRatios(final double... gearRatioValue) {
        this.gearRatio = gearRatioValue.clone();
        return this;
    }

    /**
     * VehicleBuilder with Wheel data.
     *
     * @param mass wheel mass [kg]
     * @param radius wheel radius [m]
     * @return VehicleBuilder
     */
    public VehicleBuilder withWheel(final double mass, final double radius) {
        this.wheelMass = mass;
        this.wheelRadius = radius;
        return this;
    }

    /**
     * VehicleBuilder with rolling resistance.
     *
     * @param enable true value if you want your model to inclue rolling resistance, false otherwise
     * @param coeff rolling resistance coefficient [Nm/(rad/s)]
     * @return VehicleBuilder
     */
    public VehicleBuilder withRollingResistance(final boolean enable, final double coeff) {
        this.enableRollingResistance = enable;
        this.rollingCoeff = coeff;
        return this;
    }

    /**
     * VehicleBuilder with bench brake.
     *
     * @param provider BrakeTorqueProvider used to control bench brake torque
     * @return VehicleBuilder
     */
    public VehicleBuilder withBenchBrake(final BrakeTorqueProvider provider) {
        this.benchBrakeTorqueProvider = provider;
        return this;
    }

    /**
     * VehicleBuilder with weather station.
     *
     * @param ws WeatherStation implementation
     * @return VehicleBuilder
     */
    public VehicleBuilder withWeatherStation(final WeatherStation ws) {
        this.weatherStation = Objects.requireNonNull(ws);
        return this;
    }

    /**
     * VehicleBuilder with custom temperature model for the engine.
     *
     * @param model TemperatureModel
     * @return VehicleBuilder
     */
    public VehicleBuilder withTemperatureModel(final TemperatureModel model) {
        this.temperatureModel = Objects.requireNonNull(model);
        return this;
    }

    /**
     * VehicleBuilder with temperature model for the engine, params injected.
     *
     * @param thermalCapacityValue thermal capacity [J/°C]
     * @param coolingCoeffValue cooling coefficient [W/°C]
     * @return VehicleBuilder
     */
    public VehicleBuilder withThermalParams(final double thermalCapacityValue,
                                            final double coolingCoeffValue) {
        this.thermalCapacity = thermalCapacityValue;
        this.coolingCoeff = coolingCoeffValue;
        return this;
    }

    /**
     * building of a VehicleImpl with non null requirements for:
     * - baseTorque
     * - torquePerRad
     * - engineInertia
     * - gearRatio
     * - wheelMass
     * - wheelRadius
     * - benchBrakeTorqueProvider
     * - weatherStation
     * - TemperatureModel.
     *
     * @return VehicleBuilder
     */
    public VehicleImpl buildVehiclewithRigidModel() {
        Objects.requireNonNull(baseTorque, "baseTorque");
        Objects.requireNonNull(torquePerRad, "torquePerRad");
        Objects.requireNonNull(engineInertia, "engineInertia");
        Objects.requireNonNull(gearRatio, "gearRatios");
        Objects.requireNonNull(wheelMass, "wheelMass");
        Objects.requireNonNull(wheelRadius, "wheelRadius");
        Objects.requireNonNull(benchBrakeTorqueProvider, "benchBrakeTorqueProvider");
        Objects.requireNonNull(weatherStation, "weatherStation");
        if (gearRatio.length == 0) {
            throw new IllegalArgumentException("at least one gearRatio");
        }
        //if no custom TemperatureModel
        TemperatureModel tm = this.temperatureModel;
        if (tm == null) {
            tm = new SimpleTemperatureModel(weatherStation.getTemperature(), thermalCapacity, coolingCoeff);
        }

        //computing the inertia of the power train
        final double ratio = gearRatio[0];
        final double wheelInertia = wheelMass * wheelRadius * wheelRadius;
        final double inertiaEq = engineInertia + wheelInertia / (ratio * ratio);
        final Engine engine = new EngineImpl(inertiaEq, new SimpleTorqueMap(baseTorque, torquePerRad), tm, weatherStation);
        final Transmission transmission = new ManualTransmission(gearRatio);
        final List<LoadModel> loads = new ArrayList<>();
        if (enableRollingResistance) {
            loads.add(new RollingResistance(rollingCoeff));
        }
        loads.add(new BenchLoad(benchBrakeTorqueProvider));

        final DriveTrain sim = new RigidDriveTrainSim(engine, transmission, loads);

        return new VehicleImpl(sim, weatherStation, wheelRadius, 0);
    }
}

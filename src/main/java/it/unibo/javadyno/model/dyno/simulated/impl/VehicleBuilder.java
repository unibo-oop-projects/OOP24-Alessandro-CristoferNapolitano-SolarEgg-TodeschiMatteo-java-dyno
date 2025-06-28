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

public class VehicleBuilder {
    // --- powertrain parameters ---
    /** base torque [N*m] */
    private Double baseTorque;
    /** torque increase per rad/s [N*m/(rad/s)] */
    private Double torquePerRad;
    /** engine rotational inertia [kg*m^2] */
    private Double engineInertia;
    /** transmission gear ratios */
    private double[] gearRatio;
    
    // --- thermal model parameters (with defaults) ---
    /** ambient start temperature for the thermal model [°C] */
    private double ambientStart    = 20;
    /** thermal capacity [J/°C] */
    private double thermalCapacity = 100000;
    /** cooling coefficient [W/°C] */
    private double coolingCoeff    = 500;
    /** custom temperature model, if injected */
    private TemperatureModel temperatureModel;
    
    // --- wheel parameters ---
    /** wheel mass [kg] */
    private Double wheelMass;
    /** wheel radius [m] */
    private Double wheelRadius;

    // --- rolling resistance ---
    /** enable rolling resistance */
    private boolean enableRollingResistance = false;
    /** rolling resistance coefficient [N*m/(rad/s)] */
    private Double rollingCoeff;

    // --- bench brake torque ---
    private BrakeTorqueProvider benchBrakeTorqueProvider;

    // --- simulation timing and enviroment ---
    /** simulation step [s] */
    private double deltaTime;
    /** weather station */
    private WeatherStation weatherStation;

    private VehicleBuilder() { }

    public static VehicleBuilder builder() {
        return new VehicleBuilder();
    }

    public VehicleBuilder withBaseTorque(double baseTorque) {
        this.baseTorque = baseTorque;
        return this;
    }

    public VehicleBuilder withTorquePerRad(double torquePerRad) {
        this.torquePerRad = torquePerRad;
        return this;
    }

    public VehicleBuilder withEngineInertia(double engineInertia) {
        this.engineInertia = engineInertia;
        return this;
    }

    public VehicleBuilder withGearRatios(double... gearRatio) {
        this.gearRatio = gearRatio.clone();
        return this;
    }

    public VehicleBuilder withWheel(double mass, double radius) {
        this.wheelMass   = mass;
        this.wheelRadius = radius;
        return this;
    }

    public VehicleBuilder withRollingResistance(boolean enable, double coeff) {
        this.enableRollingResistance = enable;
        this.rollingCoeff = coeff;
        return this;
    }

    
    public VehicleBuilder withBenchBrake(BrakeTorqueProvider provider) {
        this.benchBrakeTorqueProvider  = provider;
        return this;
    }

    public VehicleBuilder withDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
        return this;
    }

    public VehicleBuilder withWeatherStation(WeatherStation ws) {
        this.weatherStation = Objects.requireNonNull(ws);
        return this;
    }

    public VehicleBuilder withTemperatureModel(TemperatureModel model) {
        this.temperatureModel = Objects.requireNonNull(model);
        return this;
    }

    public VehicleBuilder withThermalParams(double ambientStart, double thermalCapacity, double coolingCoeff) {
        this.ambientStart    = ambientStart;
        this.thermalCapacity = thermalCapacity;
        this.coolingCoeff    = coolingCoeff;
        return this;
    }

    public VehicleImpl buildVehiclewithRigidModel() {
        Objects.requireNonNull(baseTorque, "baseTorque");
        Objects.requireNonNull(torquePerRad, "torquePerRad");
        Objects.requireNonNull(engineInertia, "engineInertia");
        Objects.requireNonNull(gearRatio, "gearRatios");
        Objects.requireNonNull(wheelMass, "wheelMass");
        Objects.requireNonNull(wheelRadius, "wheelRadius");
        Objects.requireNonNull(benchBrakeTorqueProvider, "benchBrakeTorqueProvider");
        Objects.requireNonNull(deltaTime, "deltaTime");
        Objects.requireNonNull(weatherStation, "weatherStation");
        if (gearRatio.length == 0) {
            throw new IllegalArgumentException("at least one gearRatio");
        }
        //if no custom TemperatureModel
        TemperatureModel tm = this.temperatureModel;
        if (tm == null) {
            tm = new SimpleTemperatureModel(ambientStart, thermalCapacity, coolingCoeff);
        }

        //computing the inertia of the power train
        double ratio = gearRatio[0];
        double wheelInertia = wheelMass * wheelRadius * wheelRadius;
        double inertiaEq = engineInertia + wheelInertia / (ratio * ratio);
        Engine engine = new EngineImpl(inertiaEq, new SimpleTorqueMap(baseTorque, torquePerRad),tm, weatherStation);
        Transmission transmission = new ManualTransmission(gearRatio);
        List<LoadModel> loads = new ArrayList<>();
        if (enableRollingResistance) {
            loads.add(new RollingResistance(rollingCoeff));
        }
        loads.add(new BenchLoad(benchBrakeTorqueProvider));

        DriveTrain sim = new RigidDriveTrainSim(engine, transmission, loads, deltaTime);

        return new VehicleImpl(sim, weatherStation, wheelRadius, Double.valueOf(0));
    }
}

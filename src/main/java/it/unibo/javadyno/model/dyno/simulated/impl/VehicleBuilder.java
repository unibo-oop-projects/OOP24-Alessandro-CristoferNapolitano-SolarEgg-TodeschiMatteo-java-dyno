package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unibo.javadyno.model.dyno.simulated.api.DriveTrain;
import it.unibo.javadyno.model.dyno.simulated.api.Engine;
import it.unibo.javadyno.model.dyno.simulated.api.LoadModel;
import it.unibo.javadyno.model.dyno.simulated.api.Transmission;
import it.unibo.javadyno.model.dyno.simulated.api.WeatherStation;

public class VehicleBuilder {
    private double baseTorque;
    private double torquePerRad;
    private double engineInertia;
    private double[] gearRatio;
    
    private double wheelMass;
    private double wheelRadius;

    private boolean enableRollingResistance = false;
    private double rollingCoeff;

    //TODO : waiting for exact implementation of bench
    //private double benchBrakeTorque;
    //private LiveBenchController benchController;

    private double deltaTime;
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

    /*
    public VehicleBuilder withBenchBrake(double brakeTorque, LiveBenchController ctrl) {
        this.benchBrakeTorque = brakeTorque;
        this.benchController  = ctrl;
        return this;
    }*/

    public VehicleBuilder withDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
        return this;
    }

    public VehicleBuilder withWeatherStation(WeatherStation ws) {
        this.weatherStation = Objects.requireNonNull(ws);
        return this;
    }

    public VehicleImpl buildVehiclewithRigidModel() {
        Objects.requireNonNull(baseTorque, "baseTorque");
        Objects.requireNonNull(torquePerRad, "torquePerRad");
        Objects.requireNonNull(engineInertia, "engineInertia");
        Objects.requireNonNull(gearRatio, "gearRatios");
        Objects.requireNonNull(wheelMass, "wheelMass");
        Objects.requireNonNull(wheelRadius, "wheelRadius");
        //Objects.requireNonNull(benchBrakeTorque, "benchBrakeTorque");
        //Objects.requireNonNull(benchController, "benchController");
        Objects.requireNonNull(deltaTime, "deltaTime");
        Objects.requireNonNull(weatherStation, "weatherStation");
        if (gearRatio.length == 0) {
            throw new IllegalArgumentException("at least one gearRatio");
        }

        //computing the inertia of the power train
        double ratio = gearRatio[0];
        double wheelInertia = wheelMass * wheelRadius * wheelRadius;
        double inertiaEq = engineInertia + wheelInertia / (ratio * ratio);

        Engine engine = new EngineImpl(inertiaEq, new SimpleTorqueMap(baseTorque, torquePerRad));
        Transmission transmission = new ManualTransmission(gearRatio);
        List<LoadModel> loads = new ArrayList<>();
        if (enableRollingResistance) {
            loads.add(new RollingResistance(rollingCoeff));
        }
        //loads.add(new Bench(benchBrakeTorque, benchController)); ??? TODO: updating bench

        DriveTrain sim = new RigidDriveTrainSim(engine, transmission, loads, deltaTime);

        return new VehicleImpl(sim, weatherStation, wheelRadius, Double.valueOf(0));
    }
}

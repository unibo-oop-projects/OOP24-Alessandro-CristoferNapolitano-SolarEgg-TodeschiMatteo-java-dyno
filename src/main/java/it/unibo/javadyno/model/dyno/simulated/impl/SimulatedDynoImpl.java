package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.Objects;
import java.util.Optional;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.controller.impl.AlertMonitor;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.dyno.simulated.api.BrakeTorqueProvider;
import it.unibo.javadyno.model.dyno.simulated.api.SimulatedDyno;
import it.unibo.javadyno.model.dyno.simulated.api.Vehicle;
import it.unibo.javadyno.model.dyno.simulated.api.WeatherStation;

/**
 * Implementation of the simulated Dyno.
 */
public class SimulatedDynoImpl implements SimulatedDyno {

    private static final String SIMULATED_DYNO_THREAD_NAME = "SimulatedDynoThread";
    private static final double DEFAULT_TEMPERATURE = 20.0;
    private static final double DEFAULT_THERMAL_CAPACITY = 100_000.0;
    private static final double DEFAULT_THERMAL_RESISTANCE = 500.0;
    private static final double FULL_THROTTLE = 1.0;
    private static final double UPDATE_DELTA = 0.1;
    private final Controller controller;
    private Thread simulationThread;
    private Vehicle vehicle;
    private volatile long updateTimeDelta;
    private volatile boolean running;
    private volatile RawData datas;

    /**
     * Constructor.
     *
     * @param controller the controller that will be used to retrieve user settings
     */
    public SimulatedDynoImpl(final Controller controller) {
        this.controller = controller;
        this.running = false;
        this.simulationThread = null;
        this.vehicle = null;
        this.datas = null;
    }

    /**
     * Start the simulation in a new Thread checking if the simulation is already running.
     */
    @Override
    public void begin() {
        if (!running) {
            this.updateTimeDelta = (long) controller.getUserSettings().getSimulationUpdateTimeDelta();
            this.running = true;
            final BrakeTorqueProvider bench = new BenchBrakeTorqueHolder();
            final WeatherStation weatherStation = new WeatherStationImpl(
                controller.getUserSettings().getAirTemperature(),
                (int) controller.getUserSettings().getAirPressure(),
                (int) controller.getUserSettings().getAirHumidity()
            );
            this.vehicle = VehicleBuilder.builder()
                .withBaseTorque(controller.getUserSettings().getBaseTorque())
                .withTorquePerRad(controller.getUserSettings().getTorquePerRad())
                .withEngineInertia(controller.getUserSettings().getEngineInertia())
                .withGearRatios(controller.getUserSettings().getGearRatios())
                .withWheel(
                    controller.getUserSettings().getWheelMass(),
                    controller.getUserSettings().getWheelRadius())
                .withBenchBrake(bench)
                .withWeatherStation(weatherStation)
                .withThermalParams(DEFAULT_TEMPERATURE, DEFAULT_THERMAL_CAPACITY, DEFAULT_THERMAL_RESISTANCE)
                .buildVehiclewithRigidModel();
            this.vehicle.setThrottle(FULL_THROTTLE);
            this.simulationThread = new Thread(this, SIMULATED_DYNO_THREAD_NAME);
            this.simulationThread.start();
        }
    }

    /**
     * This method stops the simulation thread if it is running.
     * It sets the running flag to false to indicate that the simulation has stopped.
     */
    @Override
    public void end() {
        if (Objects.nonNull(simulationThread)) {
            this.simulationThread.interrupt();
        }
        this.running = false;
    }

    /**
     * Check if the simulation is running.
     *
     * @return true if running, false otherwise
     */
    @Override
    public boolean isActive() {
        return this.running;
    }

    /**
     * {@inheritDoc}
     * This method run the simulation in a separate thread.
     */
    @Override
    public void run() {
        this.datas = vehicle.getRawData();
        while (this.running && Objects.nonNull(this.datas)
                && this.datas.engineRPM().get() < controller.getUserSettings().getMaxRpmSimulation()
            ) {
            this.vehicle.update(UPDATE_DELTA);
            this.datas = vehicle.getRawData();
            try {
                Thread.sleep(this.updateTimeDelta);
            } catch (final InterruptedException e) {
                this.end();
            }
        }
        this.end();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RawData getRawData() {
        if (Objects.isNull(this.datas)) {
            AlertMonitor.errorNotify(
                "Unable to retrieve datas form Simulated Dyno",
                Optional.empty()
            );
            return RawData.builder().build();
        } else {
            return this.datas;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSource getDynoType() {
        return DataSource.SIMULATED_DYNO;
    }
}

package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.Objects;
import java.util.Optional;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.controller.impl.AlertMonitor;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.dyno.simulated.api.Bench;
import it.unibo.javadyno.model.dyno.simulated.api.SimulatedDyno;
import it.unibo.javadyno.model.dyno.simulated.api.Vehicle;
import it.unibo.javadyno.model.dyno.simulated.api.WeatherStation;

/**
 * Implementation of the simulated Dyno.
 */
public class SimulatedDynoImpl implements SimulatedDyno {

    private static final String SIMULATED_DYNO_THREAD_NAME = "SimulatedDynoThread";
    private final Controller controller;
    private long updateTimeDelta;
    private Thread simulationThread;
    private Vehicle vehicle;
    private Bench bench;
    private WeatherStation weatherStation;
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
        this.bench = null;
        this.weatherStation = null;
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
            this.bench = new BenchImpl();
            this.vehicle = VehicleBuilder.builder()
                .withBaseTorque(controller.getUserSettings().getBaseTorque())
                .withTorquePerRad(controller.getUserSettings().getTorquePerRad())
                .withEngineInertia(controller.getUserSettings().getEngineInertia())
                .withGearRatios(controller.getUserSettings().getGearRatios())
                .withWheel(
                    controller.getUserSettings().getWheelMass(),
                    controller.getUserSettings().getWheelRadius())
                .withBenchBrake(null)
                .withWeatherStation(null)
                .buildVehiclewithRigidModel();
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
        while (this.running) {
            this.datas = vehicle.getRawData();
            try {
                Thread.sleep(this.updateTimeDelta);
            } catch (final InterruptedException e) {
                this.end();
            }
        }
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
        }
        return this.datas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSource getDynoType() {
        return DataSource.SIMULATED_DYNO;
    }
}

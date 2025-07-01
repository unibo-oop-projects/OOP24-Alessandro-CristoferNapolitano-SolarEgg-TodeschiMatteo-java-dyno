package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.Objects;
import java.util.Optional;
import java.time.Instant;

import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.dyno.simulated.api.Bench;
import it.unibo.javadyno.model.dyno.simulated.api.DriveTrain;
import it.unibo.javadyno.model.dyno.simulated.api.SimulatedDyno;
import it.unibo.javadyno.model.dyno.simulated.api.Vehicle;
import it.unibo.javadyno.model.dyno.simulated.api.WeatherStation;

/**
 * Implementation of the simulated Dyno.
 */
public class SimulatedDynoImpl implements SimulatedDyno {

    private static final String SIMULATED_DYNO_THREAD_NAME = "SimulatedDynoThread";
    private static final int ENGINE_RPM = 2000;
    private static final double ENGINE_TEMPERATURE = 90.0;
    private static final int UPDATE_TIME_DELTA = 10; // in milliseconds
    private volatile boolean running;
    private Thread simulationThread;
    private Vehicle vehicle;
    private Bench bench;
    private DriveTrain driveTrain;
    private WeatherStation weatherStation;
    private volatile RawData datas;

    /**
     * Constructor.
     */
    public SimulatedDynoImpl() {
        this.running = false;
        this.simulationThread = null;
        this.vehicle = null;
        this.bench = null;
        this.driveTrain = null;
        this.weatherStation = null;
        this.datas = null;
    }

    /**
     * Start the simulation in a new Thread checking if the simulation is already running.
     */
    @Override
    public void begin() {
        if (!running) {
            this.running = true;
            this.bench = new BenchImpl();
            this.vehicle = VehicleBuilder.builder()
                .withBaseTorque(ENGINE_RPM)
                .withTorquePerRad(0.0)
                .withEngineInertia(ENGINE_RPM)
                .withGearRatios(null)
                .withWheel(ENGINE_TEMPERATURE, ENGINE_RPM)
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
            this.datas = RawData.builder()
                    .timestamp(Optional.of(Instant.now()))
                    .engineRPM(Optional.of(ENGINE_RPM))
                    .engineTemperature(Optional.of(ENGINE_TEMPERATURE))
                    .rollerRPM(Optional.of(this.bench.getRollerRPM()))
                    .build();
            try {
                Thread.sleep(UPDATE_TIME_DELTA);
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
        return Objects.requireNonNull(this.datas, "RawData not initialized");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSource getDynoType() {
        return DataSource.SIMULATED_DYNO;
    }
}

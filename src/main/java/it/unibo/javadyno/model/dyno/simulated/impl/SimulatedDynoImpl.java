package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.Objects;
import java.util.Optional;

import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.dyno.simulated.api.Bench;
import it.unibo.javadyno.model.dyno.simulated.api.SimulatedDyno;

/**
 * Implementation of the simumlated Dyno.
 */
public class SimulatedDynoImpl implements SimulatedDyno {

    private static final int ENGINE_RPM = 2000;
    private static final double ENGINE_TEMPERATURE = 90.0;
    private volatile boolean running;
    private Thread simulationThread;
    private Bench bench;
    private volatile RawData datas;

    /**
     * Constructor.
     */
    public SimulatedDynoImpl() {
        this.running = false;
        this.simulationThread = null;
        this.bench = null;
    }

    /**
     * Start the simulation in a new Thread checking if the simulation is already running.
     */
    @Override
    public void begin() {
        if (!running) {
            this.running = true;
            this.bench = new BenchImpl();
            this.simulationThread = new Thread(this);
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
                    .engineRPM(Optional.of(ENGINE_RPM))
                    .engineTemperature(Optional.of(ENGINE_TEMPERATURE))
                    .rollerRPM(Optional.of(this.bench.getRollerRPM()))
                    .build();
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                this.end();
                break;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDynoType'");
    }
}

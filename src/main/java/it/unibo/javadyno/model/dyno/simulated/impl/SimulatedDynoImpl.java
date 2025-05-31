package it.unibo.javadyno.model.dyno.simulated.impl;

import java.util.Objects;
import it.unibo.javadyno.model.dyno.simulated.api.SimulatedDyno;

/**
 * Implementation of the simumlated Dyno.
 */
public class SimulatedDynoImpl implements SimulatedDyno, Runnable {

    private volatile boolean running;
    private Thread simulationThread;

    /**
     * Constructor.
     */
    public SimulatedDynoImpl() {
        this.running = false;
        this.simulationThread = null;
    }

    /**
     * Start the simulation in a new Thread.
     */
    public void startSimulation() {
        if (!running) {
            running = true;
            simulationThread = new Thread(this);
            simulationThread.start();
        }
    }

    /**
     * Stop the simulation.
     */
    public void stopSimulation() {
        if (Objects.nonNull(simulationThread)) {
            simulationThread.interrupt();
        }
        running = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEngineRPM() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEngineRPM'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRollerRPM() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRollerRPM'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getEngineTemp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEngineTemp'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLoadCellValue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLoadCellValue'");
    }
}

package it.unibo.javadyno.controller.impl;

import java.util.Objects;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.DataCollector;
import it.unibo.javadyno.model.data.api.DataElaborator;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.DataTransreciever;
import it.unibo.javadyno.model.data.impl.DataCollectorImpl;
import it.unibo.javadyno.model.data.impl.DataElaboratorImpl;
import it.unibo.javadyno.model.data.impl.DataTransreceiverImpl;
import it.unibo.javadyno.model.dyno.api.Dyno;
import it.unibo.javadyno.model.dyno.simulated.impl.SimulatedDynoImpl;
import it.unibo.javadyno.view.impl.MainMenu;
import it.unibo.javadyno.view.impl.SimulationView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Controller implementation.
 */
public class ControllerImpl implements Controller {

    private static final String SIMULATION_POLLING_THREAD_NAME = "SimulationPollingThread";
    private Dyno dyno; // Initialize with a simulated dyno
    private final DataCollector dataCollector;
    private final DataTransreciever dataTransreciever;
    private final DataElaborator dataElaborator;

    /**
     * Default constructor for ControllerImpl.
     */
    public ControllerImpl() {
        this.dyno = null;
        this.dataTransreciever = new DataTransreceiverImpl();
        this.dataElaborator = new DataElaboratorImpl(this.dataTransreciever);
        this.dataCollector = new DataCollectorImpl(this.dataElaborator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void launchApp(final String[] args) {
        Application.launch(MainMenu.class, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showSimulationView(final Stage stage) {
        this.dyno = new SimulatedDynoImpl();
        final SimulationView simulationView = new SimulationView(this);
        simulationView.start(stage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeApp() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSimulation() {
        if (!Objects.nonNull(this.dyno) || !this.dyno.isActive()) {
            this.dataCollector.clearData();
            this.dataTransreciever.begin(this.dyno, DataSource.SIMULATED_DYNO);
            this.dyno.begin();
            Thread.ofVirtual()
                .start(this::simulationPolling)
                .setName(SIMULATION_POLLING_THREAD_NAME);
        } else {
            throw new IllegalStateException("Simulation is already running."); // maybe alert box?
        }
    }

    /**
     * Polling method for the simulation.
     * This method runs in a loop while the dyno is active, collecting data and updating the graphics.
     */
    private void simulationPolling() {
        while (Objects.nonNull(dyno) && dyno.isActive()) {
            //TODO Call the DataCollector to collect data
            //TODO Update Graphics
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopSimulation() {
        Objects.requireNonNull(this.dyno, "Dyno must be initialized before stopping it."); // maybe alert box?
        if (this.dyno.isActive()) {
            this.dyno.end();
            this.dyno = null;
        }
    }

}

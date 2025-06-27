package it.unibo.javadyno.controller.impl;

import java.util.Objects;
import java.util.Random;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.DataCollector;
import it.unibo.javadyno.model.data.impl.DataCollectorImpl;
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

    private static final int RANDOM_DATA_MULTIPLIER = 100;
    private static final int RPM_MULTIPLIER = 800;
    private static final String SIMULATION_POLLING_THREAD_NAME = "SimulationPollingThread";
    private final Random random = new Random();
    private final DataCollector dataCollector;
    private Dyno dyno;
    private SimulationView simulationView;

    /**
     * Default constructor for ControllerImpl.
     */
    public ControllerImpl() {
        this.dyno = null;
        this.simulationView = null;
        this.dataCollector = new DataCollectorImpl();
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
    public void showMainMenu(final Stage stage) {
        new MainMenu().start(stage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showSimulationView(final Stage stage) {
        this.simulationView = new SimulationView(this);
        simulationView.start(stage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeApp() {
        if (Objects.nonNull(this.dyno)) {
            this.stopSimulation();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSimulation() {
        this.dyno = new SimulatedDynoImpl();
        if (!Objects.nonNull(this.dyno) || !this.dyno.isActive()) {
            this.dataCollector.initialize(this.dyno);
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
            //RANDOM NUMER GENERATION FOR TESTING
            simulationView.updateGauges(
                this.random.nextInt(RPM_MULTIPLIER),
                this.random.nextInt(RANDOM_DATA_MULTIPLIER),
                this.random.nextInt(RANDOM_DATA_MULTIPLIER)
            );
            simulationView.updateGraph(
                this.random.nextInt(RANDOM_DATA_MULTIPLIER),
                this.random.nextInt(RANDOM_DATA_MULTIPLIER)
            );
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

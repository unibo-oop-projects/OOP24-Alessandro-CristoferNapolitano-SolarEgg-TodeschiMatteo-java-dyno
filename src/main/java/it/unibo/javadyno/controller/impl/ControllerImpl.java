package it.unibo.javadyno.controller.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.controller.api.NotificationType;
import it.unibo.javadyno.model.data.api.DataCollector;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.impl.DataCollectorImpl;
import it.unibo.javadyno.model.dyno.api.Dyno;
import it.unibo.javadyno.model.dyno.simulated.impl.SimulatedDynoImpl;
import it.unibo.javadyno.view.impl.MainMenu;
import it.unibo.javadyno.view.impl.SimulationView;
import it.unibo.javadyno.view.impl.SimulationViewV2;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    private SimulationViewV2 simulationView;

    /**
     * Default constructor for ControllerImpl.
     */
    public ControllerImpl() {
        AlertMonitor.setController(this);
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
        if (Objects.nonNull(this.dyno) && this.dyno.isActive()) {
            AlertMonitor.warningNotify(
                "Simulation is running",
                Optional.of("Please stop the current simulation before returning to the main menu."
            ));
            return;
        }
        new MainMenu().start(stage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showSimulationView(final Stage stage) {
        this.simulationView = new SimulationViewV2(this);
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
        if (!Objects.nonNull(this.dyno) || !this.dyno.getDynoType().equals(DataSource.SIMULATED_DYNO)) {
            this.dyno = new SimulatedDynoImpl();
        }
        if (!this.dyno.isActive()) {
            this.dataCollector.initialize(this.dyno);
            this.dyno.begin();
            Thread.ofVirtual()
                .start(this::polling)
                .setName(SIMULATION_POLLING_THREAD_NAME);
        } else {
            AlertMonitor.warningNotify(
                "Simulation is already running",
                Optional.of("Please stop the current simulation before starting a new one."
            ));
        }
    }

    /**
     * Polling method for the simulation.
     * This method runs in a loop while the dyno is active, collecting data and updating the graphics.
     */
    private void polling() {
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
        if (Objects.isNull(this.dyno)) {
            AlertMonitor.warningNotify(
                "Simulation is not running",
                Optional.of("Dyno must be initialized before stopping it."
            ));
            return;
        }
        if (this.dyno.isActive()) {
            this.dyno.end();
            this.dyno = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showAlert(final NotificationType type, final String message, final Optional<String> explanation) {
        Platform.runLater(() -> {
            final Alert alert = new Alert(switch (type) {
                case WARNING -> AlertType.WARNING;
                case ERROR -> AlertType.ERROR;
                default -> AlertType.INFORMATION;
            });
            alert.setTitle(type.getType());
            alert.setHeaderText(message);
            explanation.ifPresent(alert::setContentText);
            alert.showAndWait();
        });
    }
}

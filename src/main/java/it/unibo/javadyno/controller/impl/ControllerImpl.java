package it.unibo.javadyno.controller.impl;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.controller.api.NotificationType;
import it.unibo.javadyno.model.data.api.DataCollector;
import it.unibo.javadyno.model.data.api.DataElaborator;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.impl.DataCollectorImpl;
import it.unibo.javadyno.model.data.impl.DataElaboratorImpl;
import it.unibo.javadyno.model.dyno.api.Dyno;
import it.unibo.javadyno.model.dyno.simulated.impl.SimulatedDynoImpl;
import it.unibo.javadyno.view.api.View;
import it.unibo.javadyno.view.impl.MainMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller implementation.
 */
public class ControllerImpl implements Controller {

    private static final int MAX_RPM = 7000;
    private static final String SIMULATION_POLLING_THREAD_NAME = "SimulationPollingThread";
    private final DataCollector dataCollector;
    private DataElaborator dataElaborator;
    private boolean isRunning;
    private Dyno dyno;
    private View view;

    /**
     * Default constructor for ControllerImpl.
     */
    public ControllerImpl() {
        AlertMonitor.setController(this);
        this.dyno = null;
        this.view = null;
        this.dataCollector = new DataCollectorImpl();
        this.dataElaborator = new DataElaboratorImpl(new TestOBD2Dyno());
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
    public void showView(final Stage stage, final View suppliedView) {
        this.view = suppliedView;
        suppliedView.begin(stage);
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
            this.dataElaborator = new DataElaboratorImpl(new TestOBD2Dyno());
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
        this.isRunning = true;
        this.dataElaborator.getElaboratedData();
        while (Objects.nonNull(dyno) && dyno.isActive() && this.isRunning) {
            //TODO Call the DataCollector to collect data
            //TODO Update Graphics
            //RANDOM NUMER GENERATION FOR TESTING
            // Update the gauges and graph with random data for testing purposes
            view.update(this.dataElaborator.getElaboratedData());
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        this.isRunning = false;
        view.update(this.dataElaborator.getElaboratedData());
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
    public boolean isPollingRunning() {
        return this.isRunning;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void importData() {
        final List<ElaboratedData> list = new LinkedList<>();
        final Random rand = new Random();
        for (int i = 0; i < MAX_RPM; i++) {
            final RawData rawData = RawData.builder()
                    .engineRPM(Optional.of(i))
                    .vehicleSpeed(Optional.of(i / 10))
                    .timestamp(Optional.of(Instant.now()))
                    .build();
            final ElaboratedData elaboratedData = new ElaboratedData(
                rawData,
                0.0,
                i * 10 + rand.nextDouble() * 1000,
                i * 15 - rand.nextDouble() * 1000
            );
            list.add(elaboratedData);
        }
        view.update(Collections.unmodifiableList(list));
    }

    private final class TestOBD2Dyno implements Dyno {

        // Initial values
        private static final Integer INITIAL_ENGINE_RPM = 500;
        private static final Integer INITIAL_VEHICLE_SPEED = 2;
        private static final Integer INITIAL_AMBIENT_TEMPERATURE = 20;
        private static final Integer INITIAL_BARO_PRESSURE = 101;

        // Engine RPM constants
        private static final double RPM_INCREASE_FACTOR = 1.05;
        private static final int MAX_ENGINE_RPM = 7000;

        // Vehicle speed constants
        private static final int MAX_VEHICLE_SPEED = 180;
        private static final int MAX_SPEED_INCREASE = 8;
        private static final int MIN_SPEED_INCREASE = 1;

        // Timestamp constants
        private static final int MIN_DELAY_MILLIS = 800;
        private static final int MAX_DELAY_MILLIS = 1100;
        private RawData prevRawData;
        private final Random rand = new Random();

        TestOBD2Dyno() {
            this.prevRawData = RawData.builder()
                    .engineRPM(Optional.of(INITIAL_ENGINE_RPM))
                    .vehicleSpeed(Optional.of(INITIAL_VEHICLE_SPEED))
                    .ambientAirTemperature(Optional.of(INITIAL_AMBIENT_TEMPERATURE))
                    .baroPressure(Optional.of(INITIAL_BARO_PRESSURE))
                    .timestamp(Optional.of(Instant.now()))
                    .build();
        }

        @Override
        public RawData getRawData() {
            // Calculate dynamic engine RPM - increase by percentage each call, with max limit
            final int currentRpm = this.prevRawData.engineRPM().get();
            final Integer newRpm = Math.min(MAX_ENGINE_RPM, (int) (currentRpm * RPM_INCREASE_FACTOR));

            // Calculate dynamic vehicle speed - logarithmic growth for realistic acceleration
            // Fast initial acceleration that slows down as it approaches max speed
            final int currentSpeed = this.prevRawData.vehicleSpeed().get();
            final double accelerationFactor = 1.0 - (double) currentSpeed / MAX_VEHICLE_SPEED;
            final int speedIncrease = Math.max(MIN_SPEED_INCREASE, (int) (MAX_SPEED_INCREASE * accelerationFactor));
            final Integer newSpeed = Math.min(MAX_VEHICLE_SPEED, currentSpeed + speedIncrease);

            // Keep ambient temperature and barometric pressure constant
            final Integer ambientTemp = this.prevRawData.ambientAirTemperature().get();
            final Integer baroPressure = this.prevRawData.baroPressure().get();

            // Generate realistic timestamp with variable delay between measurements
            final Instant prevTimestamp = this.prevRawData.timestamp().get();
            final int delayMillis = (int) (MIN_DELAY_MILLIS
                + this.rand.nextDouble()
                * (MAX_DELAY_MILLIS - MIN_DELAY_MILLIS));
            final Instant newTimestamp = prevTimestamp.plusMillis(delayMillis);

            final RawData rawData = RawData.builder()
                    .engineRPM(Optional.of(newRpm))
                    .vehicleSpeed(Optional.of(newSpeed))
                    .ambientAirTemperature(Optional.of(ambientTemp))
                    .baroPressure(Optional.of(baroPressure))
                    .timestamp(Optional.of(newTimestamp))
                    .build();
            this.prevRawData = rawData;
            return rawData;
        }

        @Override
        public DataSource getDynoType() {
            return DataSource.OBD2;
        }

        @Override
        public void begin() {
        }

        @Override
        public void end() {
        }

        @Override
        public boolean isActive() {
            return true;
        }
    }
}

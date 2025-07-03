package it.unibo.javadyno.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.controller.api.NotificationType;
import it.unibo.javadyno.model.data.api.DataCollector;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.api.UserSettingDef;
import it.unibo.javadyno.model.data.api.UserSettings;
import it.unibo.javadyno.model.data.communicator.impl.JsonWebSocketCommunicator;
import it.unibo.javadyno.model.data.impl.DataCollectorImpl;
import it.unibo.javadyno.model.dyno.api.Dyno;
import it.unibo.javadyno.model.dyno.obd2.impl.OBD2Dyno;
import it.unibo.javadyno.model.dyno.real.impl.RealDynoImpl;
import it.unibo.javadyno.model.filemanager.api.FileManager;
import it.unibo.javadyno.model.filemanager.api.FileStrategyFactory;
import it.unibo.javadyno.model.filemanager.impl.FileManagerImpl;
import it.unibo.javadyno.model.filemanager.impl.FileStrategyFactoryImpl;
import it.unibo.javadyno.view.api.View;
import it.unibo.javadyno.view.impl.MainMenu;
import it.unibo.javadyno.view.impl.component.AlertDisplayer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Controller implementation.
 */
public final class ControllerImpl implements Controller {

    private static final String SIMULATION_POLLING_THREAD_NAME = "SimulationPollingThread";
    private static final String SETTINGS_FILE_NAME = "user-settings.ser";
    private static final String PROJECT_DIR_NAME = "javadyno";
    private final DataCollector dataCollector;
    private final FileManager fileManager;
    private final FileStrategyFactory strategyFactory;
    private final UserSettings userSettings;
    private final String appDir;
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
        final String userHome = System.getProperty("user.home");
        this.appDir = userHome + File.separator + PROJECT_DIR_NAME;
        this.dataCollector = new DataCollectorImpl();
        this.fileManager = new FileManagerImpl();
        this.strategyFactory = new FileStrategyFactoryImpl();
        this.userSettings = loadUserSettingsFromFile(SETTINGS_FILE_NAME);
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
            this.stopEvaluation();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startEvaluation(final DataSource dynoType) {
        if (!Objects.nonNull(this.dyno) || !this.dyno.getDynoType().equals(dynoType)) {
            switch (dynoType) {
                case SIMULATED_DYNO -> this.dyno = new TestRealDyno();
                case OBD2 -> this.dyno = new OBD2Dyno();
                case REAL_DYNO -> this.dyno = new RealDynoImpl(new JsonWebSocketCommunicator());
            }
        }
        if (!this.dyno.isActive()) {
            this.dataCollector.initialize(this.dyno, this.userSettings);
            this.dyno.begin();
            this.isRunning = true;
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
        if (this.dyno.getDynoType().equals(DataSource.OBD2)) {
            this.dataCollector.collectData();
        }
        while (Objects.nonNull(dyno) && dyno.isActive() && this.isRunning) {
            final ElaboratedData data = this.dataCollector.collectData();
            if (Objects.nonNull(data)) {
                this.view.update(data);
            } else {
                this.stopEvaluation();
                AlertMonitor.warningNotify(
                    "Raw data incoherent",
                    Optional.of("The raw data collected is not coherent, try again by restarting the evaluation.")
                );
            }
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                this.stopEvaluation();
            }
        }
        this.isRunning = false;
        this.view.update(dataCollector.collectData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportCurrentData(final File file) {
        // Get all collected data from the dataCollector as a List.
        final List<ElaboratedData> currentData = dataCollector.getFullData();

        if (currentData.isEmpty()) {
            AlertMonitor.warningNotify(
                "No Data found to Export",
                Optional.of("No simulation data is available for export. Please run a simulation first.")
            );
            return;
        }

        try {
            // Factory determines strategy based on file extension.
            final var strategy = strategyFactory.createStrategyFor(file);

            if (strategy.isEmpty()) {
                AlertMonitor.errorNotify(
                    "Unsupported File Format",
                    Optional.of("The selected file format is not supported.")
                );
                return;
            }

            // Set the strategy and export the data.
            fileManager.setStrategy(strategy.get());
            fileManager.exportDataToFile(currentData, file); 

            AlertMonitor.infoNotify(
                "Export Successful!",
                Optional.of("Successfully exported " + currentData.size() + " data points to: " + file.getName())
            );

        } catch (final IOException e) {
            AlertMonitor.errorNotify(
                "Export Failed :(",
                Optional.of("Failed to export data: " + e.getMessage())
            );
        } catch (final IllegalStateException e) {
            AlertMonitor.errorNotify(
                "Export Configuration Error",
                Optional.of("FileManager strategy not properly configured: " + e.getMessage())
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void importDataFromFile(final File file) {
        try {
            // Use factory to determine strategy based on file extension.
            final var strategy = strategyFactory.createStrategyFor(file);

            if (strategy.isEmpty()) {
                AlertMonitor.errorNotify(
                    "Unsupported File Format",
                    Optional.of("The selected file format is not supported.")
                );
                return;
            }

            // Set the strategy and import the data.
            fileManager.setStrategy(strategy.get());
            final List<ElaboratedData> importedList = fileManager.importDataFromFile(file);

            if (importedList.isEmpty()) {
                AlertMonitor.warningNotify(
                    "Empty File",
                    Optional.of("The selected file is empty or doesn't have valid data.")
                );
                return;
            }

            // Update the view with the imported data.S
            if (Objects.nonNull(view)) {
                view.update(Collections.unmodifiableList(importedList));
            }

        } catch (final IOException e) {
            AlertMonitor.errorNotify(
                "Import Failed :(",
                Optional.of("Failed to import data: " + e.getMessage())
            );
        } catch (final IllegalStateException e) {
            AlertMonitor.errorNotify(
                "Import Configuration Error", 
                Optional.of("FileManager strategy not properly configured: " + e.getMessage())
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopEvaluation() {
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
            this.isRunning = false;
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
        AlertDisplayer.showAlert(type, message, explanation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSetting(final UserSettingDef setting, final double value) {
        switch (setting) {
            case SIMULATION_UPDATE_TIME_DELTA -> this.userSettings.setSimulationUpdateTimeDelta(value);
            case SIMULATION_MAX_RPM -> this.userSettings.setSimulationMaxRPM(value);
            case LOADCELL_LEVER_LENGTH -> this.userSettings.setLoadcellLeverLength(value);
            case VEHICLE_MASS -> this.userSettings.setVehicleMass(value);
            case ROLLING_RESISTANCE_COEFFICIENT -> this.userSettings.setRollingResistanceCoefficient(value);
            case AIR_DRAG_COEFFICIENT -> this.userSettings.setAirDragCoefficient(value);
            case FRONTAL_AREA -> this.userSettings.setFrontalArea(value);
            case AIR_DENSITY -> this.userSettings.setAirDensity(value);
            case DRIVE_TRAIN_EFFICIENCY -> this.userSettings.setDriveTrainEfficiency(value);
            case DYNO_TYPE -> this.userSettings.setDynoType(value);
            case BASE_TORQUE -> this.userSettings.setBaseTorque(value);
            case TORQUE_PER_RAD -> this.userSettings.setTorquePerRad(value);
            case ENGINE_INERTIA -> this.userSettings.setEngineInertia(value);
            case GEAR_RATIOS -> this.userSettings.setGearRatios(new double[]{value});
            case WHEEL_MASS -> this.userSettings.setWheelMass(value);
            case WHEEL_RADIUS -> this.userSettings.setWheelRadius(value);
            case ROLLING_COEFF -> this.userSettings.setRollingCoeff(value);
            case AIR_TEMPERATURE -> this.userSettings.setAirTemperature(value);
            case AIR_PRESSURE -> this.userSettings.setAirPressure(value);
            case AIR_HUMIDITY -> this.userSettings.setAirHumidity(value);
        }
        saveUserSettingsToFile(SETTINGS_FILE_NAME, this.userSettings);
    }

    @Override
    public UserSettings getUserSettings() {
        return this.userSettings.copy();
    }

    @Override
    public void resetUserSettings() {
        this.userSettings.resetToDefaults();
        saveUserSettingsToFile(SETTINGS_FILE_NAME, this.userSettings);
    }

    /**
     * Loads user settings from a serialized file in the user home directory.
     * If the file doesn't exist or cannot be loaded, returns a new UserSettings with default values.
     *
     * @param settingsFileName the name of the file to load settings from
     * @return the loaded UserSettings or a new instance with defaults
     */
    private UserSettings loadUserSettingsFromFile(final String settingsFileName) {
        final String filePath = appDir + File.separator + settingsFileName;
        final File file = new File(filePath);

        if (!file.exists()) {
            return new UserSettings();
        }

        try (var fileInputStream = new FileInputStream(file);
            var objectInputStream = new ObjectInputStream(fileInputStream)) {
            final Object loadedObject = objectInputStream.readObject();
            if (loadedObject instanceof UserSettings) {
                return (UserSettings) loadedObject;
            } else {
                return new UserSettings();
            }

        } catch (final IOException | ClassNotFoundException e) {
            AlertMonitor.errorNotify(
                "Could not load user settings",
                Optional.of("Using default settings. Error: " + e.getMessage())
            );
            return new UserSettings();
        }
    }

    /**
     * Saves user settings to a serialized file.
     * If the file cannot be saved, shows an error notification.
     *
     * @param settingsFileName the name of the file to save settings to
     * @param settings the UserSettings object to save
     */
    private void saveUserSettingsToFile(final String settingsFileName, final UserSettings settings) {
        final File directory = new File(appDir);

        if (!directory.exists() && !directory.mkdirs()) {
            AlertMonitor.errorNotify(
                "Could not create application directory",
                Optional.of("Failed to create directory: " + appDir)
            );
            return;
        }

        final String filePath = appDir + File.separator + settingsFileName;

        try (var fileOutputStream = new FileOutputStream(filePath);
            var objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(settings);
            objectOutputStream.flush();

        } catch (final IOException e) {
            AlertMonitor.errorNotify(
                "Could not save user settings",
                Optional.of("Settings were not saved. Error: " + e.getMessage())
            );
        }
    }

    /**
     * Test implementation of a Real Dyno for simulation purposes.
     * This class generates realistic dyno data with torque and engine RPM
     */
    private final class TestRealDyno implements Dyno {
        private static final Integer INITIAL_ENGINE_RPM = 1000;
        private static final Double INITIAL_TORQUE = 50.0;
        private static final Double INITIAL_ENGINE_TEMPERATURE = 85.0;
        private static final Double INITIAL_THROTTLE_POSITION = 10.0;
        private static final double RPM_INCREASE_FACTOR = 1.03;
        private static final double MAX_TORQUE = 400.0;
        private static final double TORQUE_PEAK_RPM = 3500.0;
        private static final double TORQUE_VARIATION = 15.0;
        private static final double TEMP_INCREASE_RATE = 0.1;
        private static final double MAX_ENGINE_TEMPERATURE = 105.0;
        private static final double MIN_ENGINE_TEMPERATURE = 80.0;
        private static final double THROTTLE_INCREASE_RATE = 1.5;
        private static final double MAX_THROTTLE_POSITION = 100.0;
        private static final double MIN_THROTTLE_POSITION = 5.0;
        private static final double TRANSMISSION_RATIO = 0.3;
        private static final double WHEEL_RADIUS = 0.3;
        private static final int MIN_DELAY_MILLIS = 200;
        private static final int MAX_DELAY_MILLIS = 400;
        private final int maxEngineRPM = (int) userSettings.getSimulationMaxRPM();
        private final Random rand = new Random();
        private RawData prevRawData;
        private boolean isActive;

        TestRealDyno() {
            this.prevRawData = RawData.builder()
                    .engineRPM(Optional.of(INITIAL_ENGINE_RPM))
                    .torque(Optional.of(INITIAL_TORQUE))
                    .engineTemperature(Optional.of(INITIAL_ENGINE_TEMPERATURE))
                    .throttlePosition(Optional.of(INITIAL_THROTTLE_POSITION))
                    .timestamp(Optional.of(Instant.now()))
                    .build();
        }

        @Override
        public RawData getRawData() {
            if (!isActive) {
                return prevRawData;
            }

            final int currentRpm = this.prevRawData.engineRPM().get();
            final Integer newRpm;
            newRpm = Math.min(maxEngineRPM, (int) (currentRpm * RPM_INCREASE_FACTOR));
            if (newRpm >= maxEngineRPM) {
                isActive = false;
            }

            final double rpmRatio = newRpm / TORQUE_PEAK_RPM;
            final double baseTorque;
            if (rpmRatio <= 1.0) {
                baseTorque = INITIAL_TORQUE + (MAX_TORQUE - INITIAL_TORQUE) * Math.sin(rpmRatio * Math.PI / 2);
            } else {
                baseTorque = MAX_TORQUE * Math.cos((rpmRatio - 1.0) * Math.PI / 4);
            }

            final double torqueVariation = (rand.nextDouble() - 0.5) * TORQUE_VARIATION;
            final Double newTorque = Math.max(10.0, baseTorque + torqueVariation);

            final double currentTemp = this.prevRawData.engineTemperature().get();
            final double tempIncrease = (newRpm > 4000) ? TEMP_INCREASE_RATE : -TEMP_INCREASE_RATE * 0.3;
            final Double newTemperature = Math.max(MIN_ENGINE_TEMPERATURE, 
                Math.min(MAX_ENGINE_TEMPERATURE, currentTemp + tempIncrease + rand.nextGaussian() * 0.5));

            final double currentThrottle = this.prevRawData.throttlePosition().get();
            final double throttleChange = THROTTLE_INCREASE_RATE;
            final Double newThrottlePosition = Math.max(MIN_THROTTLE_POSITION,
                Math.min(MAX_THROTTLE_POSITION, currentThrottle + throttleChange + rand.nextGaussian() * 2.0));

            final Instant prevTimestamp = this.prevRawData.timestamp().get();
            final int delayMillis = (int) (MIN_DELAY_MILLIS
                + this.rand.nextDouble() * (MAX_DELAY_MILLIS - MIN_DELAY_MILLIS));
            final Instant newTimestamp = prevTimestamp.plusMillis(delayMillis);

            final double speedMS = newRpm * TRANSMISSION_RATIO * WHEEL_RADIUS * 2 * Math.PI / 60.0;
            final int speedKMH = (int) (speedMS * 3.6);

            final RawData rawData = RawData.builder()
                    .engineRPM(Optional.of(newRpm))
                    .torque(Optional.of(newTorque))
                    .engineTemperature(Optional.of(newTemperature))
                    .throttlePosition(Optional.of(newThrottlePosition))
                    .vehicleSpeed(Optional.of(speedKMH))
                    .timestamp(Optional.of(newTimestamp))
                    .build();

            this.prevRawData = rawData;
            return rawData;
        }

        @Override
        public DataSource getDynoType() {
            return DataSource.REAL_DYNO;
        }

        @Override
        public void begin() {
            this.isActive = true;
        }

        @Override
        public void end() {
            this.isActive = false;
        }

        @Override
        public boolean isActive() {
            return this.isActive;
        }
    }
}

package it.unibo.javadyno.view.impl;

import java.util.List;
import java.util.Objects;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.UserSettingDef;
import it.unibo.javadyno.model.data.api.UserSettings;
import it.unibo.javadyno.view.api.View;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Settings view class for configuring user preferences in the JavaDyno application.
 */
public class SettingsView extends Application implements View {
    private static final String CSS_FILE = "css/SettingsStyle.css";
    private static final String TITLE = "Settings";
    private static final String SETTING_LABEL_STYLE = "setting-label";
    private static final String BUTTON_STYLE = "button";
    private static final double WIDTH_RATIO = 0.5;
    private static final double HEIGHT_RATIO = 0.6;
    private static final double MIN_VALUE = 0.01;
    private static final double MAX_VALUE = 10_000.0;
    private static final double STEP = 0.01;
    private static final double EFFICIENCY_MIN = 0.1;
    private static final double VEHICLE_MASS_STEP = 1.0;
    private static final double RPM_MIN = 1000.0;
    private static final double RPM_MAX = 10_500.0;
    private static final double RPM_STEP = 100.0;
    private static final double CONTAINER_PADDING = 30.0;
    private static final double CONTAINER_SPACING = 20.0;
    private static final double BUTTON_PANEL_SPACING = 15.0;
    private static final double GRID_SPACING = 15.0;
    private static final double LABEL_MIN_WIDTH = 200.0;
    private static final double TITLE_FONT_SIZE = 24.0;
    private final Controller controller;
    private final UserSettings userSettings;
    private Spinner<Double> loadcellLeverLengthSpinner;
    private Spinner<Double> vehicleMassSpinner;
    private Spinner<Double> driveTrainEfficiencySpinner;
    private Spinner<Double> maxSimulationRpmSpinner;
    private ComboBox<DataSource> dynoTypeComboBox;

    /**
     * Constructor for SettingsView.
     *
     * @param controller the controller instance
     */
    public SettingsView(final Controller controller) {
        this.controller = controller;
        this.userSettings = controller.getUserSettings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Stage primaryStage) {
        final VBox mainContainer = createMainContainer();
        final Label titleLabel = createTitleLabel();
        final GridPane settingsGrid = createSettingsGrid();
        final HBox buttonPanel = createButtonPanel(primaryStage);

        mainContainer.getChildren().addAll(titleLabel, settingsGrid, buttonPanel);

        final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        final double width = screenBounds.getWidth() * WIDTH_RATIO;
        final double height = screenBounds.getHeight() * HEIGHT_RATIO;

        final Scene scene = new Scene(mainContainer, width, height);
        scene.getStylesheets().add(ClassLoader.getSystemResource(CSS_FILE).toExternalForm());

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        controller.closeApp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(final Stage primaryStage) {
        this.start(primaryStage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final ElaboratedData data) {
        // Settings view doesn't need to handle data updates
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final List<ElaboratedData> data) {
        // Settings view doesn't need to handle data updates
    }

    /**
     * Creates the main container for the settings view.
     *
     * @return the main VBox container
     */
    private VBox createMainContainer() {
        final VBox container = new VBox(CONTAINER_SPACING);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(CONTAINER_PADDING));
        container.getStyleClass().add("settings-container");
        return container;
    }

    /**
     * Creates the title label.
     *
     * @return the title label
     */
    private Label createTitleLabel() {
        final Label titleLabel = new Label("Settings");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, TITLE_FONT_SIZE));
        titleLabel.getStyleClass().add("title-label");
        return titleLabel;
    }

    /**
     * Creates the settings grid with all controls.
     *
     * @return the settings GridPane
     */
    private GridPane createSettingsGrid() {
        final GridPane grid = new GridPane();
        grid.setHgap(GRID_SPACING);
        grid.setVgap(GRID_SPACING);
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("settings-grid");

        final ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setHalignment(HPos.RIGHT);
        labelColumn.setMinWidth(LABEL_MIN_WIDTH);

        final ColumnConstraints controlColumn = new ColumnConstraints();
        controlColumn.setHalignment(HPos.LEFT);
        controlColumn.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(labelColumn, controlColumn);

        addLoadcellLeverLengthSetting(grid, 0);
        addVehicleMassSetting(grid, 1);
        addDriveTrainEfficiencySetting(grid, 2);
        addMaxSimulationRpmSetting(grid, 3);
        addDynoTypeSetting(grid, 4);

        return grid;
    }

    /**
     * Adds the loadcell lever length setting to the grid.
     *
     * @param grid the GridPane to add to
     * @param row the row index
     */
    private void addLoadcellLeverLengthSetting(final GridPane grid, final int row) {
        final Label label = new Label("Loadcell Lever Length (m):");
        label.getStyleClass().add(SETTING_LABEL_STYLE);

        loadcellLeverLengthSpinner = createDoubleSpinner(
            this.userSettings.getLoadcellLeverLength(),
            MIN_VALUE,
            MAX_VALUE,
            STEP
        );

        grid.add(label, 0, row);
        grid.add(loadcellLeverLengthSpinner, 1, row);
    }

    /**
     * Adds the vehicle mass setting to the grid.
     *
     * @param grid the GridPane to add to
     * @param row the row index
     */
    private void addVehicleMassSetting(final GridPane grid, final int row) {
        final Label label = new Label("Vehicle Mass (kg):");
        label.getStyleClass().add(SETTING_LABEL_STYLE);

        vehicleMassSpinner = createDoubleSpinner(
            this.userSettings.getVehicleMass(),
            MIN_VALUE,
            MAX_VALUE,
            VEHICLE_MASS_STEP
        );

        grid.add(label, 0, row);
        grid.add(vehicleMassSpinner, 1, row);
    }

    /**
     * Adds the drive train efficiency setting to the grid.
     *
     * @param grid the GridPane to add to
     * @param row the row index
     */
    private void addDriveTrainEfficiencySetting(final GridPane grid, final int row) {
        final Label label = new Label("Drive Train Efficiency (0-1):");
        label.getStyleClass().add(SETTING_LABEL_STYLE);

        driveTrainEfficiencySpinner = createDoubleSpinner(
            this.userSettings.getDriveTrainEfficiency(),
            EFFICIENCY_MIN,
            1.0,
            STEP
        );

        grid.add(label, 0, row);
        grid.add(driveTrainEfficiencySpinner, 1, row);
    }

    /**
     * Adds the dyno type setting to the grid.
     *
     * @param grid the GridPane to add to
     * @param row the row index
     */
    private void addDynoTypeSetting(final GridPane grid, final int row) {
        final Label label = new Label("Dyno Type:");
        label.getStyleClass().add(SETTING_LABEL_STYLE);

        dynoTypeComboBox = new ComboBox<>();
        dynoTypeComboBox.getItems().addAll(DataSource.OBD2, DataSource.REAL_DYNO);
        dynoTypeComboBox.setValue(this.userSettings.getDynoType());
        dynoTypeComboBox.getStyleClass().add("combo-box");

        dynoTypeComboBox.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(final DataSource item, final boolean empty) {
                super.updateItem(item, empty);
                if (empty || Objects.isNull(item)) {
                    setText(null);
                } else {
                    setText(item.getActualName());
                }
            }
        });

        dynoTypeComboBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(final DataSource item, final boolean empty) {
                super.updateItem(item, empty);
                if (empty || Objects.isNull(item)) {
                    setText(null);
                } else {
                    setText(item.getActualName());
                }
            }
        });

        grid.add(label, 0, row);
        grid.add(dynoTypeComboBox, 1, row);
    }

    /**
     * Adds the max simulation RPM setting to the grid.
     *
     * @param grid the GridPane to add to
     * @param row the row index
     */
    private void addMaxSimulationRpmSetting(final GridPane grid, final int row) {
        final Label label = new Label("Max Simulation RPM:");
        label.getStyleClass().add(SETTING_LABEL_STYLE);

        maxSimulationRpmSpinner = createDoubleSpinner(
            this.userSettings.getSimulationMaxRPM(),
            RPM_MIN,
            RPM_MAX,
            RPM_STEP
        );

        grid.add(label, 0, row);
        grid.add(maxSimulationRpmSpinner, 1, row);
    }

    /**
     * Creates a double spinner with the specified parameters.
     *
     * @param initialValue the initial value
     * @param min the minimum value
     * @param max the maximum value
     * @param step the step amount
     * @return the configured Spinner
     */
    private Spinner<Double> createDoubleSpinner(final double initialValue, final double min, 
                                               final double max, final double step) {
        final SpinnerValueFactory<Double> valueFactory = 
            new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue, step);
        final Spinner<Double> spinner = new Spinner<>(valueFactory);
        spinner.setEditable(true);
        spinner.getStyleClass().add("spinner");
        return spinner;
    }

    /**
     * Creates the button panel with Save, Reset, and Cancel buttons.
     *
     * @param primaryStage the primary stage for navigation
     * @return the button panel HBox
     */
    private HBox createButtonPanel(final Stage primaryStage) {
        final HBox buttonPanel = new HBox(BUTTON_PANEL_SPACING);
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.getStyleClass().add("button-panel");

        final Button saveButton = new Button("Save");
        saveButton.getStyleClass().addAll(BUTTON_STYLE, "save-button");
        saveButton.setOnAction(e -> saveSettings());

        final Button resetButton = new Button("Reset to Defaults");
        resetButton.getStyleClass().addAll(BUTTON_STYLE, "reset-button");
        resetButton.setOnAction(e -> resetToDefaults());

        final Button backButton = new Button("Back to Menu");
        backButton.getStyleClass().addAll(BUTTON_STYLE, "back-button");
        backButton.setOnAction(e -> controller.showMainMenu(primaryStage));

        buttonPanel.getChildren().addAll(saveButton, resetButton, backButton);
        return buttonPanel;
    }

    /**
     * Saves the current settings values.
     */
    private void saveSettings() {
        controller.updateSetting(UserSettingDef.LOADCELL_LEVER_LENGTH, loadcellLeverLengthSpinner.getValue());
        controller.updateSetting(UserSettingDef.VEHICLE_MASS, vehicleMassSpinner.getValue());
        controller.updateSetting(UserSettingDef.DRIVE_TRAIN_EFFICIENCY, driveTrainEfficiencySpinner.getValue());
        controller.updateSetting(UserSettingDef.DYNO_TYPE, dynoTypeComboBox.getValue().ordinal());
        controller.updateSetting(UserSettingDef.SIMULATION_MAX_RPM, maxSimulationRpmSpinner.getValue());
    }

    /**
     * Resets all settings to their default values.
     */
    private void resetToDefaults() {
        this.controller.resetUserSettings();
        loadCurrentSettings();
    }

    /**
     * Loads current settings into the UI controls.
     */
    private void loadCurrentSettings() {
        loadcellLeverLengthSpinner.getValueFactory().setValue(this.userSettings.getLoadcellLeverLength());
        vehicleMassSpinner.getValueFactory().setValue(this.userSettings.getVehicleMass());
        driveTrainEfficiencySpinner.getValueFactory().setValue(this.userSettings.getDriveTrainEfficiency());
        dynoTypeComboBox.setValue(this.userSettings.getDynoType());
        maxSimulationRpmSpinner.getValueFactory().setValue(this.userSettings.getSimulationMaxRPM());
    }
}

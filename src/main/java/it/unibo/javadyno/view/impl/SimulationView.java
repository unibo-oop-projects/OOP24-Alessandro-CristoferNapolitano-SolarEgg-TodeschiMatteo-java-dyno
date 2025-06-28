package it.unibo.javadyno.view.impl;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.view.api.View;
import it.unibo.javadyno.view.impl.component.ChartsPanel;
import it.unibo.javadyno.view.impl.component.GaugePanel;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Simulation view class for the JavaDyno application.
 */
public class SimulationView extends Application implements View {
    private static final String STAGE_TITLE = "JavaDyno - Simulation";
    private static final String CSS_FILE = "/css/simulationStyle.css";
    private static final String CSS_SETTINGS_PANEL_TAG = "left-column";
    private static final String CSS_MAIN_CONTAINER_TAG = "main-container";
    private static final int COLUMN_SPACING = 5;
    private static final double WIDTH_RATIO = 0.8; //percentage of screen width
    private static final double HEIGHT_RATIO = 0.8; //percentage of screen height

    private final Controller controller;
    private final VBox settingsPanel = new VBox(COLUMN_SPACING);
    private final ChartsPanel chartsPanel = new ChartsPanel();
    private final GaugePanel gaugePanel = new GaugePanel();

    /**
     * Constructor for SimulationView that imports the controller.
     *
     * @param controller the controller to be used
     */
    public SimulationView(final Controller controller) {
        this.controller = controller;
    }

    /**
     * Draw the simulation view interface.
     */
    @Override
    public void start(final Stage primaryStage) {
        final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        final double width = screenBounds.getWidth() * WIDTH_RATIO;
        final double height = screenBounds.getHeight() * HEIGHT_RATIO;
        settingsPanel.setAlignment(Pos.CENTER);
        settingsPanel.getStyleClass().add(CSS_SETTINGS_PANEL_TAG);
        HBox.setHgrow(settingsPanel, Priority.ALWAYS);
        HBox.setHgrow(chartsPanel, Priority.ALWAYS);
        HBox.setHgrow(gaugePanel, Priority.ALWAYS);
        VBox.setVgrow(chartsPanel, Priority.ALWAYS);
        VBox.setVgrow(gaugePanel, Priority.NEVER); 

        // Setting up buttons for the left column
        final Button startSimulationButton = new Button("Start Simulation");
        startSimulationButton.setId("start-button");
        final Button stopSimulationButton = new Button("Stop Simulation");
        stopSimulationButton.setId("stop-button");
        final Button saveDataButton = new Button("Save datas");
        final Button backToMenuButton = new Button("Back to menu");
        final Button reloadButton = new Button("Reload simulation");
        stopSimulationButton.setDisable(true);
        saveDataButton.setDisable(true);
        startSimulationButton.setOnAction(e -> {
            controller.startSimulation();
            startSimulationButton.setDisable(true);
            stopSimulationButton.setDisable(false);
        });
        stopSimulationButton.setOnAction(e -> {
            controller.stopSimulation();
            stopSimulationButton.setDisable(true);
            saveDataButton.setDisable(false);
            settingsPanel.getChildren().removeAll(startSimulationButton, stopSimulationButton);
            settingsPanel.getChildren().add(0, reloadButton);
        });
        reloadButton.setOnAction(e -> {
            controller.showSimulationView(primaryStage);
        });
        backToMenuButton.setOnAction(e -> {
            controller.showMainMenu(primaryStage);
        });
        settingsPanel.getChildren().addAll(startSimulationButton, stopSimulationButton, saveDataButton, backToMenuButton);

        // Create the main container
        final HBox mainContainer = new HBox();
        mainContainer.getStyleClass().add(CSS_MAIN_CONTAINER_TAG);
        final VBox leftPanel = new VBox();
        final VBox rightPanel = new VBox();
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        rightPanel.setAlignment(Pos.TOP_RIGHT);
        rightPanel.setSpacing(0); 
        leftPanel.getChildren().add(settingsPanel);
        rightPanel.getChildren().addAll(chartsPanel, gaugePanel);
        mainContainer.getChildren().addAll(leftPanel, rightPanel);

        // Set the scene
        final Scene scene = new Scene(mainContainer, width, height);
        scene.getStylesheets().add(SimulationView.class.getResource(CSS_FILE).toExternalForm());
        primaryStage.setTitle(STAGE_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    /**
     * Updates the graph with new data points.
     *
     * @param xValue the x-axis value to be added to the graph
     * @param yValue the y-axis value to be added to the graph
     */
    public void updateGraph(final Number xValue, final Number yValue) {
        this.chartsPanel.addPointToChart(xValue, yValue);
    }

    /**
     * Updates the gauges with new values.
     *
     * @param rpm the current RPM value
     * @param speed the current speed value
     * @param temperature the current temperature value
     */
    public void updateGauges(final int rpm, final int speed, final double temperature) {
        this.gaugePanel.updateGauges(rpm, speed, temperature);
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
    public void update(final ElaboratedData data) {
        updateGauges(data.rawData().engineRPM().orElse(0),
                     data.rawData().vehicleSpeed().orElse(0),
                     data.rawData().engineTemperature().orElse(0.0));
        updateGraph(data.rawData().engineRPM().orElse(0),
                    data.rawData().torque().orElse(0.0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(final Stage primaryStage) {
        this.start(primaryStage);
    }
}

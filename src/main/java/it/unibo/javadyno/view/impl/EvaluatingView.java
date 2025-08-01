package it.unibo.javadyno.view.impl;

import java.util.List;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.view.api.View;
import it.unibo.javadyno.view.impl.component.ButtonsPanel;
import it.unibo.javadyno.view.impl.component.LabelsType;
import it.unibo.javadyno.view.impl.component.ChartsPanel;
import it.unibo.javadyno.view.impl.component.GaugePanel;
import it.unibo.javadyno.view.impl.component.StatsPanel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Simulation view class for the JavaDyno application.
 */
public class EvaluatingView extends Application implements View {

    public static final double EVALUATING_RATIO = 0.8;

    private static final String CSS_FILE = "css/SimulationStyle.css";
    private static final String CSS_SETTINGS_PANEL_TAG = "left-column";
    private static final String CSS_MAIN_CONTAINER_TAG = "main-container";
    private static final String CSS_BUTTONS_TAG = "buttons-panel";
    private static final String CSS_STATS_TAG = "stats-panel";

    private final Controller controller;
    private final LabelsType buttonsType;
    private final DataSource dataSource;
    private final ChartsPanel chartsPanel = new ChartsPanel();
    private final GaugePanel gaugePanel = new GaugePanel();
    private final StatsPanel statsPanel = new StatsPanel();
    private ButtonsPanel buttonsPanel;

    /**
     * Constructor for SimulationView that imports the controller.
     *
     * @param controller the controller to be used
     * @param type the type of buttons to be created
     * @param dataSource the data source to be used for the simulation
     */
    public EvaluatingView(final Controller controller, final LabelsType type, final DataSource dataSource) {
        this.controller = controller;
        this.buttonsType = type;
        this.dataSource = dataSource;
    }

    /**
     * Draw the simulation view interface.
     */
    @Override
    public void start(final Stage primaryStage) {
        final HBox mainContainer = new HBox();
        final VBox leftPanel = new VBox();
        final VBox rightPanel = new VBox();
        buttonsPanel = new ButtonsPanel(controller, primaryStage, buttonsType, dataSource);

        HBox.setHgrow(leftPanel, Priority.NEVER);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        HBox.setHgrow(chartsPanel, Priority.ALWAYS);
        HBox.setHgrow(gaugePanel, Priority.ALWAYS);
        VBox.setVgrow(chartsPanel, Priority.ALWAYS);
        VBox.setVgrow(gaugePanel, Priority.NEVER);
        VBox.setVgrow(buttonsPanel, Priority.ALWAYS);
        VBox.setVgrow(statsPanel, Priority.ALWAYS);
        buttonsPanel.getStyleClass().add(CSS_BUTTONS_TAG);
        statsPanel.getStyleClass().add(CSS_STATS_TAG);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.getStyleClass().add(CSS_SETTINGS_PANEL_TAG);
        leftPanel.getChildren().addAll(buttonsPanel, statsPanel);
        rightPanel.setAlignment(Pos.TOP_RIGHT);
        rightPanel.setSpacing(0);
        rightPanel.getChildren().addAll(chartsPanel, gaugePanel);
        mainContainer.getStyleClass().add(CSS_MAIN_CONTAINER_TAG);
        mainContainer.getChildren().addAll(leftPanel, rightPanel);

        final Scene scene = new Scene(mainContainer);
        scene.getStylesheets().add(ClassLoader.getSystemResource(CSS_FILE).toExternalForm());
        primaryStage.setTitle(this.buttonsType.getTitle());
        primaryStage.setScene(scene);
        primaryStage.show();
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
     * Updates the graph with new data points.
     *
     * @param xValue the x-axis value to be added to the graph
     * @param yValue the y-axis value to be added to the graph
     * @param y2Value the second y-axis value to be added to the graph
     */
    private void updateGraph(final Number xValue, final Number yValue, final Number y2Value) {
        this.chartsPanel.addSingleData(xValue, yValue, y2Value);
    }

    /**
     * Updates the gauges with new values.
     *
     * @param rpm the current RPM value
     * @param speed the current speed value
     * @param temperature the current temperature value
     */
    private void updateGauges(final int rpm, final int speed, final double temperature) {
        this.gaugePanel.updateGauges(rpm, speed, temperature);
    }

    private void updateStats(final int rpm, final double torque, final double horsePower, final double kiloWatts) {
        this.statsPanel.updateStats(rpm, torque, horsePower, kiloWatts);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final ElaboratedData data) {
        if (!controller.isPollingRunning()) {
            Platform.runLater(buttonsPanel::reachedEnd);
            return;
        }
        Platform.runLater(() -> {
            updateGauges(data.rawData().engineRPM().orElse(0),
                        data.rawData().vehicleSpeed().orElse(0),
                        data.rawData().engineTemperature().orElse(0.0));
            updateGraph(data.rawData().engineRPM().orElse(0),
                        data.enginePowerHP(),
                        data.engineCorrectedTorque());
            updateStats(data.rawData().engineRPM().orElse(0),
                        data.engineCorrectedTorque(),
                        data.enginePowerHP(),
                        data.enginePowerKW());
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final List<ElaboratedData> data) {
        Platform.runLater(() -> {
            this.chartsPanel.addAllData(
                data.stream()
                    .map(i -> (Number) i.rawData().engineRPM().orElse(0))
                    .toList(),
                data.stream()
                    .map(i -> (Number) i.enginePowerHP())
                    .toList(),
                data.stream()
                    .map(i -> (Number) i.engineCorrectedTorque())
                    .toList()
                );
        });
    }

}

package it.unibo.javadyno.view.impl;

import eu.hansolo.medusa.Gauge;
import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.graph.api.GaugeFactory;
import it.unibo.javadyno.model.graph.impl.DefaultGaugeFactory;
import it.unibo.javadyno.view.api.View;
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

    private static final int CONTAINER_SPACING = 20;
    private static final int COLUMN_SPACING = 5;
    private static final String CSS_FILE = "/css/simulationStyle.css";
    private static final double WIDTH_RATIO = 0.8; //percentage of screen width
    private static final double HEIGHT_RATIO = 0.8; //percentage of screen height

    // Constants for the gauges
    private static final String RPM_CHARTS_TITLE = "Tachometer";
    private static final String RPM_CHARTS_UNIT = "RPM";
    private static final int RPM_MAX_RANGE = 8000;
    private static final int RPM_MAJOR_TICK_SPACE = 1000;
    private static final int RPM_MINOR_TICK_SPACE = 200;

    private static final String SPEEDOMETER_TITLE = "Speedometer";
    private static final String SPEEDOMETER_UNIT = "KM/H";
    private static final int SPEEDOMETER_MAX_RANGE = 300;
    private static final int SPEEDOMETER_MAJOR_TICK_SPACE = 20;
    private static final int SPEEDOMETER_MINOR_TICK_SPACE = 5;

    private static final String TEMPERATURE_TITLE = "Temperature";
    private static final String TEMPERATURE_UNIT = "°C";
    private static final int TEMPERATURE_MAX_RANGE = 120;
    private static final int TEMPERATURE_MAJOR_TICK_SPACE = 20;
    private static final int TEMPERATURE_MINOR_TICK_SPACE = 5;

    private final Controller controller;
    private final GaugeFactory gaugeFactory = new DefaultGaugeFactory();

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

        final HBox mainContainer = new HBox();
        mainContainer.setSpacing(CONTAINER_SPACING);
        final VBox leftColumn = new VBox(COLUMN_SPACING);
        leftColumn.setAlignment(Pos.CENTER);
        final VBox centerColumn = new VBox(COLUMN_SPACING);
        centerColumn.setAlignment(Pos.CENTER);
        final VBox rightColumn = new VBox(COLUMN_SPACING);
        rightColumn.setAlignment(Pos.CENTER);
        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        HBox.setHgrow(centerColumn, Priority.ALWAYS);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);
        leftColumn.getStyleClass().add("left-column");
        centerColumn.getStyleClass().add("center-column");
        rightColumn.getStyleClass().add("right-column");

        final Gauge rpmGauge = gaugeFactory.createGaugeChart(
                RPM_CHARTS_TITLE,
                RPM_CHARTS_UNIT,
                0,
                RPM_MAX_RANGE,
                RPM_MAJOR_TICK_SPACE,
                RPM_MINOR_TICK_SPACE
        );
        final Gauge speedGauge = gaugeFactory.createGaugeChart(
                SPEEDOMETER_TITLE,
                SPEEDOMETER_UNIT,
                0,
                SPEEDOMETER_MAX_RANGE,
                SPEEDOMETER_MAJOR_TICK_SPACE,
                SPEEDOMETER_MINOR_TICK_SPACE
        );
        final Gauge tempGauge = gaugeFactory.createGaugeChart(
                TEMPERATURE_TITLE,
                TEMPERATURE_UNIT,
                0,
                TEMPERATURE_MAX_RANGE,
                TEMPERATURE_MAJOR_TICK_SPACE,
                TEMPERATURE_MINOR_TICK_SPACE
        );
        rightColumn.getChildren().add(rpmGauge);
        rightColumn.getChildren().add(speedGauge);
        rightColumn.getChildren().add(tempGauge);

        final Button startSimulationButton = new Button("Start Simulation");
        final Button stopSimulationButton = new Button("Stop Simulation");
        final Button backToMenuButton = new Button("Back to menu");
        startSimulationButton.setOnAction(e -> controller.startSimulation());
        stopSimulationButton.setOnAction(e -> controller.stopSimulation());
        backToMenuButton.setOnAction(e -> controller.showMainMenu((Stage) backToMenuButton.getScene().getWindow()));

        leftColumn.getChildren().addAll(startSimulationButton, stopSimulationButton, backToMenuButton);
        mainContainer.getChildren().addAll(leftColumn, centerColumn, rightColumn);

        final Scene scene = new Scene(mainContainer, width, height);
        scene.getStylesheets().add(SimulationView.class.getResource(CSS_FILE).toExternalForm());
        primaryStage.setTitle("JavaDyno - Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
}

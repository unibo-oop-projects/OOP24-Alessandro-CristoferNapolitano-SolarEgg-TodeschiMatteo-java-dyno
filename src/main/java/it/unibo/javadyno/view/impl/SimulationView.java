package it.unibo.javadyno.view.impl;

import it.unibo.javadyno.controller.api.Controller;
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

    private final Controller controller;

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

        final Button startSimulationButton = new Button("Start Simulation");
        final Button stopSimulationButton = new Button("Stop Simulation");
        final Button backToMenuButton = new Button("Back to menu");
        startSimulationButton.setOnAction(e -> controller.startSimulation());
        stopSimulationButton.setOnAction(e -> controller.stopSimulation());

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

package it.unibo.javadyno.view.impl.component;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.view.api.View;
import it.unibo.javadyno.view.impl.SimulationView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ButtonsPanel class that creates a panel with buttons for controlling the GUI.
 */
public final class ButtonsPanel extends VBox {
    final private Button startSimulationButton;
    final private Button stopSimulationButton;
    final private Button saveDataButton;
    final private Button importDataButton;
    final private Button backToMenuButton;
    final private Button reloadButton;

    /**
     * Constructor for ButtonsPanel that initializes the buttons and their actions.
     *
     * @param controller the controller to be used
     * @param primaryStage the primary stage of the application
     * @param view the view to be displayed
     */
    public ButtonsPanel(final Controller controller, final Stage primaryStage, final View view) {
        startSimulationButton = new Button("Start Simulation");
        startSimulationButton.setId("start-button");
        stopSimulationButton = new Button("Stop Simulation");
        stopSimulationButton.setId("stop-button");
        saveDataButton = new Button("Save datas");
        importDataButton = new Button("Import datas");
        backToMenuButton = new Button("Back to menu");
        reloadButton = new Button("Reload simulation");
        startSimulationButton.setOnAction(e -> {
            controller.startEvaluation(DataSource.SIMULATED_DYNO);
            this.getChildren().remove(startSimulationButton);
            this.getChildren().addFirst(stopSimulationButton);
        });
        stopSimulationButton.setOnAction(e -> {
            controller.stopEvaluation();
            reachedEnd();
        });
        importDataButton.setOnAction(e -> {
            controller.importData();
        });
        reloadButton.setOnAction(e -> {
            controller.showView(primaryStage, new SimulationView(controller));
        });
        backToMenuButton.setOnAction(e -> {
            controller.showMainMenu(primaryStage);
        });
        this.getChildren().addAll(startSimulationButton, backToMenuButton);
    }

    /**
     * Method to be called when the dyno run ends.
     */
    public void reachedEnd() {
        this.getChildren().remove(stopSimulationButton);
        this.getChildren().addFirst(importDataButton);
        this.getChildren().addFirst(saveDataButton);
        this.getChildren().addFirst(reloadButton);
    }
}

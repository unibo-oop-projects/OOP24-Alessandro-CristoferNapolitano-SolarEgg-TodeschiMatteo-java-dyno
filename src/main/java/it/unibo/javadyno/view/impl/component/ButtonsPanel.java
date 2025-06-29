package it.unibo.javadyno.view.impl.component;

import it.unibo.javadyno.controller.api.Controller;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ButtonsPanel class that creates a panel with buttons for controlling the GUI.
 */
public class ButtonsPanel extends VBox {
    public ButtonsPanel(final Controller controller, final Stage primaryStage) {
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
            this.getChildren().removeAll(startSimulationButton, stopSimulationButton);
            this.getChildren().add(0, reloadButton);
        });
        reloadButton.setOnAction(e -> {
            controller.showSimulationView(primaryStage);
        });
        backToMenuButton.setOnAction(e -> {
            controller.showMainMenu(primaryStage);
        });
        this.getChildren().addAll(startSimulationButton, stopSimulationButton, saveDataButton, backToMenuButton);
    }
}

package it.unibo.javadyno.view.impl.component;

import it.unibo.javadyno.controller.api.Controller;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ButtonsPanel class that creates a panel with buttons for controlling the GUI.
 */
public final class ButtonsPanel extends VBox {
    /**
     * Constructor for ButtonsPanel that initializes the buttons and their actions.
     *
     * @param controller the controller to be used
     * @param primaryStage the primary stage of the application
     */
    public ButtonsPanel(final Controller controller, final Stage primaryStage) {
        final Button startSimulationButton = new Button("Start Simulation");
        startSimulationButton.setId("start-button");
        final Button stopSimulationButton = new Button("Stop Simulation");
        stopSimulationButton.setId("stop-button");
        final Button saveDataButton = new Button("Save datas");
        final Button backToMenuButton = new Button("Back to menu");
        final Button reloadButton = new Button("Reload simulation");
        startSimulationButton.setOnAction(e -> {
            controller.startSimulation();
            this.getChildren().remove(startSimulationButton);
            this.getChildren().addFirst(stopSimulationButton);
        });
        stopSimulationButton.setOnAction(e -> {
            controller.stopSimulation();
            this.getChildren().remove(stopSimulationButton);
            this.getChildren().addFirst(saveDataButton);
            this.getChildren().addFirst(reloadButton);
        });
        reloadButton.setOnAction(e -> {
            controller.showView(primaryStage);
        });
        backToMenuButton.setOnAction(e -> {
            controller.showMainMenu(primaryStage);
        });
        this.getChildren().addAll(startSimulationButton, backToMenuButton);
    }
}

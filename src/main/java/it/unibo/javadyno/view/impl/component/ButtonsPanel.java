package it.unibo.javadyno.view.impl.component;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.view.impl.EvaluatingView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * ButtonsPanel class that creates a panel with buttons for controlling the GUI.
 */
public final class ButtonsPanel extends VBox {
    private final Button startSimulationButton;
    private final Button stopSimulationButton;
    private final Button saveDataButton;
    private final Button importDataButton;
    private final Button backToMenuButton;
    private final Button reloadButton;

    /**
     * Constructor for ButtonsPanel that initializes the buttons and their actions.
     *
     * @param controller the controller to be used
     * @param primaryStage the primary stage of the application
     * @param type the type of buttons to be created
     */
    public ButtonsPanel(final Controller controller, final Stage primaryStage, final LabelsType type) {
        startSimulationButton = new Button(type.getStartButton());
        startSimulationButton.setId("start-button");
        stopSimulationButton = new Button(type.getStopButton());
        stopSimulationButton.setId("stop-button");
        saveDataButton = new Button(type.getSaveButton());
        importDataButton = new Button(type.getLoadButton());
        backToMenuButton = new Button(type.getBackToMenu());
        reloadButton = new Button(type.getReloadButton());
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
            controller.importDataFromFile(new FileChooser().showOpenDialog(primaryStage));
        });
        saveDataButton.setOnAction(e -> {
            controller.exportCurrentData(new FileChooser().showSaveDialog(primaryStage));
        });
        reloadButton.setOnAction(e -> {
            controller.showView(primaryStage, new EvaluatingView(controller, type));
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

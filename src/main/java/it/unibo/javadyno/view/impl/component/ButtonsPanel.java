package it.unibo.javadyno.view.impl.component;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.view.impl.EvaluatingView;
import it.unibo.javadyno.view.impl.MainMenu;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * ButtonsPanel class that creates a panel with buttons for controlling the GUI.
 */
public final class ButtonsPanel extends VBox {

    private static final String START_BUTTON_ID = "start-button";
    private static final String STOP_BUTTON_ID = "stop-button";
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
     * @param dataSource the data source to be used for the simulation
     */
    public ButtonsPanel(
            final Controller controller, final Stage primaryStage,
            final LabelsType type, final DataSource dataSource
        ) {
        startSimulationButton = new Button(type.getStartButton());
        startSimulationButton.setId(START_BUTTON_ID);
        stopSimulationButton = new Button(type.getStopButton());
        stopSimulationButton.setId(STOP_BUTTON_ID);
        saveDataButton = new Button(type.getSaveButton());
        importDataButton = new Button(type.getLoadButton());
        backToMenuButton = new Button(type.getBackToMenu());
        reloadButton = new Button(type.getReloadButton());
        startSimulationButton.setOnAction(e -> {
            controller.startEvaluation(dataSource);
            this.getChildren().remove(startSimulationButton);
            this.getChildren().addFirst(stopSimulationButton);
        });
        stopSimulationButton.setOnAction(e -> {
            controller.stopEvaluation();
            reachedEnd();
        });

        // Uses a private method to handle file import properly.
        importDataButton.setOnAction(e -> IOUtility.handleImport(controller, primaryStage));
        // Uses a private method to handle file export properly.
        saveDataButton.setOnAction(e -> IOUtility.handleExport(controller, primaryStage));

        reloadButton.setOnAction(e -> {
            controller.showView(primaryStage, new EvaluatingView(controller, type, dataSource));
        });
        backToMenuButton.setOnAction(e -> {
            controller.showMainMenu(primaryStage);
            primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth() * MainMenu.WIDTH_RATIO);
            primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight() * MainMenu.HEIGHT_RATIO);
            primaryStage.centerOnScreen();
        });
        this.getChildren().addAll(startSimulationButton, backToMenuButton);
    }

    /**
     * Method to be called when the dyno run ends.
     */
    public void reachedEnd() {
        this.getChildren().remove(stopSimulationButton);
        this.getChildren().remove(reloadButton);
        this.getChildren().remove(saveDataButton);
        this.getChildren().remove(importDataButton);
        this.getChildren().addFirst(importDataButton);
        this.getChildren().addFirst(saveDataButton);
        this.getChildren().addFirst(reloadButton);
    }
}

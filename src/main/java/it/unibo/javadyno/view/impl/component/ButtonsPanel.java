package it.unibo.javadyno.view.impl.component;

import java.io.File;

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
     * @param dataSource the data source to be used for the simulation
     */
    public ButtonsPanel(final Controller controller, final Stage primaryStage,
            final LabelsType type, final DataSource dataSource) {
        startSimulationButton = new Button(type.getStartButton());
        startSimulationButton.setId("start-button");
        stopSimulationButton = new Button(type.getStopButton());
        stopSimulationButton.setId("stop-button");
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
        importDataButton.setOnAction(e -> handleImport(controller, primaryStage));
        // Uses a private method to handle file export properly.
        saveDataButton.setOnAction(e -> handleExport(controller, primaryStage));

        reloadButton.setOnAction(e -> {
            controller.showView(primaryStage, new EvaluatingView(controller, type, dataSource));
        });
        backToMenuButton.setOnAction(e -> {
            controller.showMainMenu(primaryStage);
        });
        this.getChildren().addAll(startSimulationButton, backToMenuButton);
    }

    /**
     * Sets proper file chooser configuration for the file export.
     *
     * @param controller the controller to handle the export
     * @param stage the stage for the file dialog
     */
    private void handleExport(final Controller controller, final Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        // Set file extension filters
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JSON files", "*.json"),
            new FileChooser.ExtensionFilter("CSV files", "*.csv")
        );

        // Show save dialog and handle the result
        final File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            controller.exportCurrentData(file);
        }
    }

    /**
     * Sets proper file chooser configuration for the file import.
     *
     * @param controller the controller to handle the import
     * @param stage the stage for the file dialog
     */
    private void handleImport(final Controller controller, final Stage stage) {
        final FileChooser fileChooser = new FileChooser();

        // Set file extension filters
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JSON files", "*.json"),
            new FileChooser.ExtensionFilter("CSV files", "*.csv")
        );

        // Show open dialog and handle the result
        final File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            // Controller handles import and automatically updates the view
            controller.importDataFromFile(file);
        }
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

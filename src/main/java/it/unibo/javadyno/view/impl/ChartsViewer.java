package it.unibo.javadyno.view.impl;

import java.util.List;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.view.api.View;
import it.unibo.javadyno.view.impl.component.LabelsType;
import it.unibo.javadyno.view.impl.component.ChartsPanel;
import it.unibo.javadyno.view.impl.component.IOUtility;
import javafx.application.Application;
import javafx.application.Platform;
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
public class ChartsViewer extends Application implements View {

    public static final double WIDTH_RATIO = 0.8;
    public static final double HEIGHT_RATIO = 0.8;

    private static final String STAGE_TITLE = "JavaDyno - Charts Viewer";
    private static final String CSS_FILE = "css/SimulationStyle.css";
    private static final String CSS_MAIN_CONTAINER_TAG = "main-container";
    private static final String CSS_BUTTONS_TAG = "buttons-panel";

    private final Controller controller;
    private final ChartsPanel chartsPanel = new ChartsPanel();

    /**
     * Constructor for SimulationView that imports the controller.
     *
     * @param controller the controller to be used
     */
    public ChartsViewer(final Controller controller) {
        this.controller = controller;
    }

    /**
     * Draw the simulation view interface.
     */
    @Override
    public void start(final Stage primaryStage) {
        final VBox mainContainer = new VBox();
        final HBox buttonsPanel = new HBox();
        VBox.setVgrow(chartsPanel, Priority.ALWAYS);
        this.chartsPanel.setChartViewerVgrow(Priority.ALWAYS);
        buttonsPanel.setAlignment(Pos.CENTER);
        final Button importDataButton = new Button(LabelsType.OBD.getLoadButton());
        final Button backToMenuButton = new Button(LabelsType.OBD.getBackToMenu());
        buttonsPanel.getStyleClass().add(CSS_BUTTONS_TAG);
        buttonsPanel.getChildren().addAll(importDataButton, backToMenuButton);
        importDataButton.setOnAction(e -> {
            IOUtility.handleImport(this.controller, primaryStage);
        });
        backToMenuButton.setOnAction(e -> {
            this.controller.showMainMenu(primaryStage);
            primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth() * MainMenu.WIDTH_RATIO);
            primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight() * MainMenu.HEIGHT_RATIO);
            primaryStage.centerOnScreen();
        });
        mainContainer.getStyleClass().add(CSS_MAIN_CONTAINER_TAG);
        mainContainer.getChildren().addAll(buttonsPanel, chartsPanel);
        this.chartsPanel.hideDefaultVisibility();

        final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        final double width = screenBounds.getWidth() * WIDTH_RATIO;
        final double height = screenBounds.getHeight() * HEIGHT_RATIO;
        final Scene scene = new Scene(mainContainer, width, height);
        scene.getStylesheets().add(ClassLoader.getSystemResource(CSS_FILE).toExternalForm());
        primaryStage.setTitle(STAGE_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        this.controller.closeApp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(final Stage primaryStage) {
        this.start(primaryStage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final ElaboratedData data) { }

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

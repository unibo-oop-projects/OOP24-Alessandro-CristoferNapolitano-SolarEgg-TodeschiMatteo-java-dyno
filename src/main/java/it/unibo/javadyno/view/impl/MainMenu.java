package it.unibo.javadyno.view.impl;

import java.util.List;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.view.api.View;
import it.unibo.javadyno.view.impl.component.LabelsType;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

/**
 * Main GUI class for the JavaDyno application.
 */
public class MainMenu extends Application implements View {
    // final Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/SimpleGui.fxml"));

    private static final String CSS_FILE = "css/MenuStyle.css";
    private static final String ICON_PATH = "images/icon.png";
    private static final String HOME_IMAGE = "images/logo_no_bg.png";
    private static final double WIDTH_RATIO = 0.3; //percentage of screen width
    private static final double HEIGHT_RATIO = 0.5; //percentage of screen height
    private static final double NEW_RATIO = 0.8; //percentage of screen width/height for new window
    private static final double IMAGE_WIDTH = 0.6;
    private static final double IMAGE_HEIGHT = 0.4;

    private static Controller controller;

    /**
     * Sets the controller for the GUI test.
     *
     * @param ctrl the controller instance to set
     */
    public static void setController(final Controller ctrl) {
        controller = ctrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Stage primaryStage) {
        final Button simulatedDynoButton = new Button("Simulation");
        final Button realDynoButton = new Button("Dyno");
        final Button chartsViewerButton = new Button("Charts");
        final Button settingsButton = new Button("Settings");
        simulatedDynoButton.setOnAction(e -> {
            controller.showView(primaryStage, new EvaluatingView(controller, LabelsType.SIMULATED, DataSource.SIMULATED_DYNO));
            primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth() * NEW_RATIO);
            primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight() * NEW_RATIO);
            primaryStage.centerOnScreen();
        });
        realDynoButton.setOnAction(e -> {
            controller.showView(primaryStage, new EvaluatingView(controller, LabelsType.REAL, DataSource.REAL_DYNO));
            primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth() * NEW_RATIO);
            primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight() * NEW_RATIO);
            primaryStage.centerOnScreen();
        });
        chartsViewerButton.setOnAction(e -> {
            controller.showView(primaryStage, new ChartsViewer(controller));
        });
        settingsButton.setOnAction(e -> {
            controller.showView(primaryStage, new SettingsView(controller));
        });

        final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        final double width = screenBounds.getWidth() * WIDTH_RATIO;
        final double height = screenBounds.getHeight() * HEIGHT_RATIO;
        final ImageView image = new ImageView(new Image(ClassLoader.getSystemResource(HOME_IMAGE).toExternalForm()));
        final VBox vbox = new VBox(15, image, realDynoButton, simulatedDynoButton, chartsViewerButton, settingsButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("main-menu-container");
        final Scene scene = new Scene(vbox, width, height);

        image.fitWidthProperty().bind(Bindings.multiply(scene.widthProperty(), IMAGE_WIDTH));
        image.fitHeightProperty().bind(Bindings.multiply(scene.heightProperty(), IMAGE_HEIGHT));
        image.setPreserveRatio(true);

        scene.getStylesheets().add(ClassLoader.getSystemResource(CSS_FILE).toExternalForm());
        primaryStage.setTitle("JavaDyno");
        primaryStage.getIcons().add(new Image(ClassLoader.getSystemResource(ICON_PATH).toExternalForm()));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
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
     * {@inheritDoc}
     */
    @Override
    public void update(final ElaboratedData data) { }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final List<ElaboratedData> data) { }
}

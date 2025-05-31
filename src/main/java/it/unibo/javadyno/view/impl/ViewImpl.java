package it.unibo.javadyno.view.impl;

import it.unibo.javadyno.view.api.View;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * {@inheritDoc}.
 */
public class ViewImpl extends Application implements View {

    private static final String HOME_IMAGE = "images/homecar.png";
    private static final double IMAGE_WIDTH = 0.6;
    private static final double IMAGE_HEIGHT = 0.4;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        // final Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/SimpleGui.fxml"));

        final Button startSimulationButton = new Button("Avvia Simulazione");
        final Button connectOBDButton = new Button("Collegamento OBD");
        final Button startTestButton = new Button("Avvia Prova");

        final ImageView image = new ImageView(new Image(ClassLoader.getSystemResource(HOME_IMAGE).toExternalForm()));
        final VBox vbox = new VBox(15, image, startSimulationButton, connectOBDButton, startTestButton);
        vbox.setAlignment(Pos.CENTER);
        final Scene scene = new Scene(vbox, 400, 400);

        image.fitWidthProperty().bind(Bindings.multiply(scene.widthProperty(), IMAGE_WIDTH));
        image.fitHeightProperty().bind(Bindings.multiply(scene.heightProperty(), IMAGE_HEIGHT));
        image.setPreserveRatio(true);

        primaryStage.setTitle("JavaDyno");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

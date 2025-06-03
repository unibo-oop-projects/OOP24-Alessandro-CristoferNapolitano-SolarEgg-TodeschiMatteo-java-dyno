package it.unibo.javadyno.view.test;

import it.unibo.javadyno.view.api.View;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Test class for the GUI of the simulation thread.
 */
public class SimulationThreadGUITest extends Application implements View {

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Button startSimulationButton = new Button("Avvia Simulazione");
        final Button stopSimulationButton = new Button("termina Simulazione");
        final VBox vbox = new VBox(15, startSimulationButton, stopSimulationButton);
        vbox.setAlignment(Pos.CENTER);
        final Scene scene = new Scene(vbox, 400, 400);

        primaryStage.setTitle("SimulationThreadGUITest");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

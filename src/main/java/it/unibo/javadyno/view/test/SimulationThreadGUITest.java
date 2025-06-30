package it.unibo.javadyno.view.test;

import java.util.List;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.ElaboratedData;
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
    public void start(final Stage primaryStage) throws Exception {
        final Button startSimulationButton = new Button("Avvia Simulazione");
        final Button stopSimulationButton = new Button("Termina Simulazione");
        startSimulationButton.setOnAction(e -> controller.startEvaluation(DataSource.SIMULATED_DYNO));
        stopSimulationButton.setOnAction(e -> controller.stopEvaluation());
        final VBox vbox = new VBox(15, startSimulationButton, stopSimulationButton);
        vbox.setAlignment(Pos.CENTER);
        final Scene scene = new Scene(vbox, 400, 400);

        primaryStage.setTitle("SimulationThreadGUITest");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(final Stage primaryStage) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'begin'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final ElaboratedData data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final List<ElaboratedData> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
}

//  SimulationThreadGUITest.setController(this);
//  Application.launch(SimulationThreadGUITest.class, args);

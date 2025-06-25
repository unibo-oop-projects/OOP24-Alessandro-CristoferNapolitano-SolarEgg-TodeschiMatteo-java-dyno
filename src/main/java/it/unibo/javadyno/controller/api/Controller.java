package it.unibo.javadyno.controller.api;

import javafx.stage.Stage;

/**
 * Controller interface.
 */
public interface Controller {

    /**
     * @param args passed to JavaFX.
     */
    void launchApp(String[] args);

    /**
     * Shows the main menu of the application.
     * @param stage the primary stage to show the main menu on
     */
    void showMainMenu(final Stage stage);

    /**
     * Shows the simulation view.
     *
     * @param stage the primary stage to show the simulation view on
     */
    void showSimulationView(Stage stage);

    /**
     * Closes the application.
     */
    void closeApp();

    /**
     * Start the simulation.
     */
    void startSimulation();

    /**
     * Stop the simulation.
     */
    void stopSimulation();

}

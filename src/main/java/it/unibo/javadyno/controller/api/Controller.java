package it.unibo.javadyno.controller.api;

import java.io.File;
import java.util.Optional;

import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.view.api.View;
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
     *
     * @param stage the primary stage to show the main menu on
     */
    void showMainMenu(Stage stage);

    /**
     * Shows the simulation view.
     *
     * @param stage the primary stage to show the simulation view on
     * @param view the view to be displayed
     */
    void showView(Stage stage, View view);

    /**
     * Closes the application.
     */
    void closeApp();

    /**
     * Starts the dyno run to evaluate the engine performance.
     *
     * @param dynoType the type of dyno to be used for evaluation
     */
    void startEvaluation(DataSource dynoType);

    /**
     * Stops the dyno run.
     */
    void stopEvaluation();

    /**
     * Checks if the polling is running.
     *
     * @return true if polling is running, false otherwise
     */
    boolean isPollingRunning();

    /**
     * Shows an alert dialog with a given message.
     *
     * @param type the type of notification
     * @param message the message to display in the alert dialog
     * @param explanation the explanation of the alert
     */
    void showAlert(NotificationType type, String message, Optional<String> explanation);

    /**
     * Exports current simulation data to a file.
     *
     * @param file The destination file.
     */
    void exportCurrentData(File file);

    /**
     * Imports data from a file and displays it in the current view.
     *
     * @param file The source file.
     */
    void importDataFromFile(File file);

    /**
     * Test method for importing data.
     */
    public void importDataTest();
}

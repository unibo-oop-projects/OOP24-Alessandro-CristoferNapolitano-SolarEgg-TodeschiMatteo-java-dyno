package it.unibo.javadyno.controller.api;

import java.util.Optional;
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
     * Start the simulation.
     */
    void startSimulation();

    /**
     * Stop the simulation.
     */
    void stopSimulation();

    /**
     * Shows an alert dialog with a given message.
     *
     * @param type the type of notification
     * @param message the message to display in the alert dialog
     * @param explanation the explanation of the alert
     */
    void showAlert(NotificationType type, String message, Optional<String> explanation);

}

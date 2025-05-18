package it.unibo.javadyno.controller.api;

/**
 * Controller interface.
 */
public interface Controller {

    /**
     * @param args passed to JavaFX.
     */
    void launchApp(String[] args);

    /**
     * Closes the application.
     */
    void closeApp();

}

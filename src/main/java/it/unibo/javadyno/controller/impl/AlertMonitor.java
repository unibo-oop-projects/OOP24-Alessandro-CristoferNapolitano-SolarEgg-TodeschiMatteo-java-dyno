package it.unibo.javadyno.controller.impl;

import it.unibo.javadyno.controller.api.Controller;

/**
 * AlertMonitor is a utility class that provides a method to show alert dialogs using controller.
 */
public final class AlertMonitor {

    private static Controller controller;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AlertMonitor() { }

    /**
     * Sets the controller to be used for showing alerts.
     *
     * @param controller the controller to set
     */
    static void setController(final Controller controller) {
        AlertMonitor.controller = controller;
    }

    /**
     * Shows an alert dialog with the specified title, message, and explanation.
     *
     * @param title the title of the alert dialog
     * @param message the message to display in the alert dialog
     * @param explanation the explanation of the alert
     */
    public static void alertNotify(final String title, final String message, final String explanation) {
        controller.showAlert(title, message, explanation);
    }
}

package it.unibo.javadyno.view.impl.component;

import java.util.Optional;

import it.unibo.javadyno.controller.api.NotificationType;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utility class to display alerts in the JavaFX application.
 */
public class AlertDisplayer {
    /**
     * Private constructor to prevent instantiation.
     */
    private AlertDisplayer() { }

    /**
     * Displays a short alert with the specified type, message, and optional explanation.
     *
     * @param type the type of notification (e.g., WARNING, ERROR, INFORMATION)
     * @param message the main message of the alert
     * @param explanation an optional explanation text to provide more context
     */
    public static void showAlert(final NotificationType type, final String message, final Optional<String> explanation) {
        Platform.runLater(() -> {
            final Alert alert = new Alert(switch (type) {
                case WARNING -> AlertType.WARNING;
                case ERROR -> AlertType.ERROR;
                default -> AlertType.INFORMATION;
            });
            alert.setTitle(type.getType());
            alert.setHeaderText(message);
            explanation.ifPresent(alert::setContentText);
            alert.showAndWait();
        });
    }
}

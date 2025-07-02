package it.unibo.javadyno.view.impl.component;

import java.io.File;
import java.util.Objects;

import it.unibo.javadyno.controller.api.Controller;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Utility class for handling file import and export operations.
 */
public final class IOUtility {

    /**
     * Private constructor to prevent instantiation.
     */
    private IOUtility() { }

    /**
     * Sets proper file chooser configuration for the file export.
     *
     * @param controller the controller to handle the export
     * @param stage the stage for the file dialog
     */
    public static void handleExport(final Controller controller, final Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JSON files", "*.json"),
            new FileChooser.ExtensionFilter("CSV files", "*.csv")
        );
        final File file = fileChooser.showSaveDialog(stage);
        if (Objects.nonNull(file)) {
            controller.exportCurrentData(file);
        }
    }

    /**
     * Sets proper file chooser configuration for the file import.
     *
     * @param controller the controller to handle the import
     * @param stage the stage for the file dialog
     */
    public static void handleImport(final Controller controller, final Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JSON files", "*.json"),
            new FileChooser.ExtensionFilter("CSV files", "*.csv")
        );
        final File file = fileChooser.showOpenDialog(stage);
        if (Objects.nonNull(file)) {
            controller.importDataFromFile(file);
        }
    }
}

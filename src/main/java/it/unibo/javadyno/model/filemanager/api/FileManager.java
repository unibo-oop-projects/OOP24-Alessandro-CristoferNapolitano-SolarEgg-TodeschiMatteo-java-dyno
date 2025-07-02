package it.unibo.javadyno.model.filemanager.api;

import it.unibo.javadyno.model.data.api.ElaboratedData;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Interface for filemanager.
 * This is the Context interface for file management.
 */
public interface FileManager {
    /**
     * Sets the Strategy for file management,
     * which will define what file type is used.
     *
     * @param strategy The file strategy to use.
     */
    void setStrategy(FileStrategy strategy);

    /**
     * Exports a list of ElaboratedData to file using the current strategy.
     *
     * @param dataList The list of data to export.
     * @param file The destination file where data will be saved.
     * @throws IOException If an I/O error occurs while writing the file.
     * @throws IllegalStateException If no strategy has been set.
     */
    void exportDataToFile(List<ElaboratedData> dataList, File file) throws IOException;

    /**
     * Imports data from a file using the current strategy.
     *
     * @param file The source file, from which data will be imported.
     * @return A list of ElaboratedData (imported from the file).
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws IllegalStateException If no strategy has been set.
     */
    List<ElaboratedData> importDataFromFile(File file) throws IOException;
}

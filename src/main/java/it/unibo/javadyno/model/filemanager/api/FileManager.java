package it.unibo.javadyno.model.filemanager.api;

import it.unibo.javadyno.model.data.api.ElaboratedData;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

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
     * Exports a queue of ElaboratedData to file using the current strategy.
     * 
     * @param dataQueue The queue of data to export.
     * @param file The destination file where data will be saved.
     * @throws IOException If an I/O error occurs or if no strategy is set.
     */
    void exportDataToFile(Queue<ElaboratedData> dataQueue, File file) throws IOException;

    /**
     * Imports data from a file using the current strategy.
     * 
     * @param file The source file, from which data will be imported.
     * @return A list of ElaboratedData (imported from the file).
     * @throws IOException If an I/O error occurs or if no strategy is set.
     */
    List<ElaboratedData> importDataFromFile(File file) throws IOException;
}

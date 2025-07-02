package it.unibo.javadyno.model.filemanager.api;

import it.unibo.javadyno.model.data.api.ElaboratedData;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The Strategy interface for FileManager.
 * Each implementation of this interface will provide the logic for reading and
 * writing a specific file format (CSV, JSON).
 */
public interface FileStrategy {

    /**
     * Exports a list of ElaboratedData to a file.
     *
     * @param data the list of ElaboratedData to be exported.
     * @param file the destination file where data will be saved.
     * @throws IOException if an I/O error occurs while writing the file.
     */
    void exportData(List<ElaboratedData> data, File file) throws IOException;

    /**
     * Imports ElaboratedData from a file as a list.
     *
     * @param file the source file from which data will be imported.
     * @return a list of ElaboratedData imported from the file.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    List<ElaboratedData> importData(File file) throws IOException;
}

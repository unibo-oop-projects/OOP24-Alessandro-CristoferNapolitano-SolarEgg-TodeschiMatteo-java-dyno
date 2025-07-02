package it.unibo.javadyno.model.filemanager.impl;

import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.filemanager.api.FileManager;
import it.unibo.javadyno.model.filemanager.api.FileStrategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A concrete implementation of the FileManager interface.
 * This class acts as the context for the Strategy pattern, delegating file
 * operations to a specific FileStrategy. It is not designed to be extended.
 */
public final class FileManagerImpl implements FileManager {

    private FileStrategy strategy;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStrategy(final FileStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportDataToFile(final List<ElaboratedData> dataList, final File file) throws IOException {
        if (this.strategy == null) {
            throw new IllegalStateException("No file strategy has been set.");
        }
        this.strategy.exportData(dataList, file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ElaboratedData> importDataFromFile(final File file) throws IOException {
        if (this.strategy == null) {
            throw new IllegalStateException("No file strategy has been set.");
        }
        return this.strategy.importData(file);
    }
}

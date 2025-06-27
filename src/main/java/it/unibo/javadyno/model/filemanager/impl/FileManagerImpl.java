package it.unibo.javadyno.model.filemanager.impl;

import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.filemanager.api.FileManager;
import it.unibo.javadyno.model.filemanager.api.FileStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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
    public void exportDataToFile(final Queue<ElaboratedData> dataQueue, final File file) throws IOException {
        if (this.strategy == null) {
            throw new IOException("No file strategy has been set.");
        }
        // The strategy works with a List, so we convert the Queue.
        this.strategy.exportData(new ArrayList<>(dataQueue), file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ElaboratedData> importDataFromFile(final File file) throws IOException {
        if (this.strategy == null) {
            throw new IOException("No file strategy has been set.");
        }
        return this.strategy.importData(file);
    }
}

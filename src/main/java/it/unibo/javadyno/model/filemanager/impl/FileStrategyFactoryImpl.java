package it.unibo.javadyno.model.filemanager.impl;

import it.unibo.javadyno.model.filemanager.api.FileStrategy;
import it.unibo.javadyno.model.filemanager.api.FileStrategyFactory;

import java.io.File;
import java.util.Optional;

/**
 * A concrete implementation of the FileStrategyFactory interface.
 * This class is responsible for creating FileStrategy instances based on the file's extension.
 * It is not designed to be extended.
 */
public final class FileStrategyFactoryImpl implements FileStrategyFactory {

    private static final String CSV_EXTENSION = ".csv";
    // TODO: private static final String JSON_EXTENSION = ".json";
    // TODO: Add more supported file types.

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FileStrategy> createStrategyFor(final File file) {
        // Shorten for readability
        final String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(CSV_EXTENSION)) {
            return Optional.of(new CsvStrategy());
        }
        // TODO: Add a future JsonStrategy:
        // if (fileName.endsWith(JSON_EXTENSION)) {
        //     return Optional.of(new JsonStrategy());
        // }
        return Optional.empty();
    }
}

package it.unibo.javadyno.model.filemanager.impl;

import it.unibo.javadyno.model.filemanager.api.FileStrategy;
import it.unibo.javadyno.model.filemanager.api.FileStrategyFactory;

import java.io.File;
import java.util.Locale;
import java.util.Optional;

/**
 * A concrete implementation of the FileStrategyFactory interface.
 * This class is responsible for creating FileStrategy instances based on the file's extension.
 * It is not designed to be extended.
 */
public final class FileStrategyFactoryImpl implements FileStrategyFactory {

    // Supported file extensions.
    private static final String CSV_EXTENSION = ".csv";
    private static final String JSON_EXTENSION = ".json";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FileStrategy> createStrategyFor(final File file) {

        // Strategy is selected based on the file's extension.

        final String fileName = file.getName().toLowerCase(Locale.ROOT);
        if (fileName.endsWith(CSV_EXTENSION)) {
            return Optional.of(new CsvStrategy());
        }

        if (fileName.endsWith(JSON_EXTENSION)) {
            return Optional.of(new JsonStrategy());
        }

        // If the file extension is not recognized, just returns an empty Optional.
        return Optional.empty();
    }
}

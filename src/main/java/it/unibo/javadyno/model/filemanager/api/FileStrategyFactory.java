package it.unibo.javadyno.model.filemanager.api;

import java.io.File;
import java.util.Optional;

/**
 * An interface for a factory that creates FileStrategy objects.
 */
@FunctionalInterface
public interface FileStrategyFactory {

    /**
     * Creates a FileStrategy based on the provided file's extension.
     *
     * @param file The file for which to choose and create a strategy.
     * @return An Optional containing the appropriate FileStrategy if a supported
     *         extension is found, otherwise an empty Optional.
     */
    Optional<FileStrategy> createStrategyFor(File file);
}

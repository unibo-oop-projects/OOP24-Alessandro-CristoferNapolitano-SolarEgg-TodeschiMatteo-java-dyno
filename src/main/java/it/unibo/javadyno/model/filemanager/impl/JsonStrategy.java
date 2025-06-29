package it.unibo.javadyno.model.filemanager.impl;

import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.filemanager.api.FileStrategy;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON Strategy implementation using the Genson library.
 * Handles reading and writing of Raw and Elaborated data to and from JSON files.
 * Uses the Genson library for parsing and writing.
 * This class is not designed for extension.
 */
public final class JsonStrategy implements FileStrategy {

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void exportData(final List<ElaboratedData> data, final File file) throws IOException {
        // A try-with-resources statement ensures the writer is automatically closed.
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            // TODO
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ElaboratedData> importData(final File file) throws IOException {
        // A try-with-resources statement ensures the reader is automatically closed.
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            // TODO
            return new ArrayList<>(); // Placeholder return
        }
    }
}

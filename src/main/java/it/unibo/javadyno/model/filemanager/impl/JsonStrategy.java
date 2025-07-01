package it.unibo.javadyno.model.filemanager.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.filemanager.api.FileStrategy;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * JSON Strategy implementation using the Jackson library.
 * Handles reading and writing of Raw and Elaborated data to and from JSON files.
 * Uses the Jackson library for serialization and deserialization.
 * Jackson supports Java records and Optional types.
 * This class is not designed for extension.
 */
public final class JsonStrategy implements FileStrategy {

    // A reusable ObjectMapper instance.
    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new Jdk8Module()) // Enables Optional<T> support.
        .registerModule(new JavaTimeModule()) // Enables Instant and other time types support.
        .enable(SerializationFeature.INDENT_OUTPUT); // Pretty printing for readability.

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportData(final List<ElaboratedData> data, final File file) throws IOException {
        objectMapper.writeValue(file, Objects.requireNonNull(data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ElaboratedData> importData(final File file) throws IOException {
        try {
            // Uses TypeReference to tell Jackson the exact generic type to deserialize to.
            // Handles the conversion to List<ElaboratedData> automatically.
            final List<ElaboratedData> importedData = objectMapper.readValue(
                file, new TypeReference<>() { }
            );

            // Returns the imported data or an empty list if the file is null.
            if (importedData != null) {
                return importedData;
            } else {
                return Collections.emptyList();
            }
        } catch (final IOException e) {
            // Re-throw with additional context about which file failed
            throw new IOException("Failed to parse JSON file: " + file.getAbsolutePath(), e);
        }
    }
}

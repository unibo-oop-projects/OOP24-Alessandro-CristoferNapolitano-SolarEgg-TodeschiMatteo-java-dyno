package it.unibo.javadyno.model.filemanager.impl;

import com.owlike.genson.Genson;
import com.owlike.genson.GenericType;
import com.owlike.genson.JsonBindingException;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.reflect.VisibilityFilter;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.filemanager.api.FileStrategy;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * JSON Strategy implementation using the Genson library.
 * Handles reading and writing of Raw and Elaborated data to and from JSON files.
 * Uses the Genson library for parsing and writing.
 * This class is not designed for extension.
 */
public final class JsonStrategy implements FileStrategy {

    /* A reusable Genson instance configured to work with Java records.
    * useIndentation to makes the JSON more readable.
    * useConstructorWithArguments to make Genson use the constructor with arguments instead of the default one.
    * useFields(true, VisibilityFilter.PRIVATE) allows Genson to access private fields directly,
    * which is necessary for records since they don't have setters.
    */
    private final Genson genson = new GensonBuilder()
        .useIndentation(true)
        .useConstructorWithArguments(true)
        .useFields(true, VisibilityFilter.PRIVATE)
        .create();

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportData(final List<ElaboratedData> data, final File file) throws IOException {
        // Using try-with-resources to make sure the writer is automatically closed.
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            genson.serialize(Objects.requireNonNull(data), writer);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ElaboratedData> importData(final File file) throws IOException {
        // Using try-with-resources to make sure the reader is automatically closed.
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            /*
             * To deserialize a generic collection like List<ElaboratedData>, provides
             * a GenericType to specify the exact target type.
             */
            final List<ElaboratedData> importedData = genson.deserialize(
                reader, new GenericType<List<ElaboratedData>>() { }
            );

            // Genson might return null.
            if (importedData != null) {
                return importedData;
            } else {
                // Returns an empty list (to ensure the caller never gets a null because of Genson.
                return Collections.emptyList();
            }

        } catch (final JsonBindingException e) {
            // If the JSON is malformed, Genson throws a JsonBindingException.
            throw new IOException("Failed to parse JSON file: " + file.getAbsolutePath(), e);
        }
    }
}

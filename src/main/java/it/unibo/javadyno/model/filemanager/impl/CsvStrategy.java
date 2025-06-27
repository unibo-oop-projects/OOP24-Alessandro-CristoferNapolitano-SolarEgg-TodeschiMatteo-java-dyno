package it.unibo.javadyno.model.filemanager.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.filemanager.api.FileStrategy;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * CSV Strategy implementation for file management.
 * Handles the reading and writing of Raw and Elaborated data to and from CSV files.
 * This class is not designed for extension.
 */
public final class CsvStrategy implements FileStrategy {

    // Defines the header of the CSV file.
    private static final String[] HEADER = {
        // RawData fields
        "timestamp", "engineRPM", "engineTemperature", "rollerRPM", "torque",
        "vehicleSpeed", "throttlePosition", "boostPressure", "exhaustGasTemperature",
        // ElaboratedData fields
        "enginePowerKW", "enginePowerHP", "engineCorrectedTorque",
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportData(final List<ElaboratedData> data, final File file) throws IOException {
       try (CSVWriter writer = new CSVWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
        writer.writeNext(HEADER); // Writes the header as the first row

        // Writes each field of ElaboratedData (and its nested RawData) as a row in the CSV file.
        for (final ElaboratedData elaborated : data) {
            final RawData raw = elaborated.rawData();
                final String[] row = {
                    // RawData fields
                    // Converts the field to string or uses an empty string if the Optional is empty.
                    raw.timestamp().map(Object::toString).orElse(""),
                    raw.engineRPM().map(Object::toString).orElse(""),
                    raw.engineTemperature().map(Object::toString).orElse(""),
                    raw.rollerRPM().map(Object::toString).orElse(""),
                    raw.torque().map(Object::toString).orElse(""),
                    raw.vehicleSpeed().map(Object::toString).orElse(""),
                    raw.throttlePosition().map(Object::toString).orElse(""),
                    raw.boostPressure().map(Object::toString).orElse(""),
                    raw.exhaustGasTemperature().map(Object::toString).orElse(""),
                    // ElaboratedData fields
                    String.valueOf(elaborated.enginePowerKW()),
                    String.valueOf(elaborated.enginePowerHP()),
                    String.valueOf(elaborated.engineCorrectedTorque()),
                };
                writer.writeNext(row);
        }
       }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ElaboratedData> importData(final File file) throws IOException {
        final List<ElaboratedData> importedData = new ArrayList<>();
        // Use try-with-resources for the reader.
        try (CSVReader reader = new CSVReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.skip(1); // Skip the header row.
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                if (fields.length < HEADER.length) {
                    continue; // Skip malformed lines.
                }

                final RawData rawData = RawData.builder()
                    .timestamp(parseOptional(fields[0], Instant::parse))
                    .engineRPM(parseOptional(fields[1], Integer::parseInt))
                    .engineTemperature(parseOptional(fields[2], Double::parseDouble))
                    .rollerRPM(parseOptional(fields[3], Integer::parseInt))
                    .torque(parseOptional(fields[4], Double::parseDouble))
                    .vehicleSpeed(parseOptional(fields[5], Integer::parseInt))
                    .throttlePosition(parseOptional(fields[6], Double::parseDouble))
                    .boostPressure(parseOptional(fields[7], Double::parseDouble))
                    .exhaustGasTemperature(parseOptional(fields[8], Double::parseDouble))
                    .build();

                final double powerKW = Double.parseDouble(fields[9]);
                final double powerHP = Double.parseDouble(fields[10]);
                final double correctedTorque = Double.parseDouble(fields[11]);

                importedData.add(new ElaboratedData(rawData, powerKW, powerHP, correctedTorque));
            }
        } catch (CsvException e) {
            // Wrap opencsv's specific exception in a standard IOException.
            throw new IOException("Error reading CSV file", e);
        }
        return importedData;
    }

    /**
     * A helper method to safely parse a string from the CSV into an Optional.
     */
    private <T> Optional<T> parseOptional(final String value, final Function<String, T> parser) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(parser.apply(value));
        } catch (final Exception e) {
            // In case of a parsing error, treat it as missing data.
            return Optional.empty();
        }
    }
}

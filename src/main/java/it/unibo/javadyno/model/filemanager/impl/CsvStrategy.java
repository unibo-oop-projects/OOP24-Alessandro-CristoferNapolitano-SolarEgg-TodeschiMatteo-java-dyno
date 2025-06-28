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
 * CSV Strategy implementation using the opencsv library.
 * Handles reading and writing of Raw and Elaborated data to and from CSV files.
 * This class is not designed for extension.
 */
public final class CsvStrategy implements FileStrategy {

    // Defines the header of the CSV file.
    private static final String[] HEADER = {
        "timestamp", "engineRPM", "engineTemperature", "rollerRPM", "torque",
        "vehicleSpeed", "throttlePosition", "boostPressure", "exhaustGasTemperature",
        "enginePowerKW", "enginePowerHP", "engineCorrectedTorque", // Trailing comma
    };

    // Column indices constants to avoid magic numbers.
    private static final int INDEX_TIMESTAMP = 0;
    private static final int INDEX_ENGINE_RPM = 1;
    private static final int INDEX_ENGINE_TEMPERATURE = 2;
    private static final int INDEX_ROLLER_RPM = 3;
    private static final int INDEX_TORQUE = 4;
    private static final int INDEX_VEHICLE_SPEED = 5;
    private static final int INDEX_THROTTLE_POSITION = 6;
    private static final int INDEX_BOOST_PRESSURE = 7;
    private static final int INDEX_EXHAUST_GAS_TEMPERATURE = 8;
    private static final int INDEX_ENGINE_POWER_KW = 9;
    private static final int INDEX_ENGINE_POWER_HP = 10;
    private static final int INDEX_ENGINE_CORRECTED_TORQUE = 11;

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportData(final List<ElaboratedData> data, final File file) throws IOException {
       try (CSVWriter writer = new CSVWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
        writer.writeNext(HEADER);

        for (final ElaboratedData elaborated : data) {
            final RawData raw = elaborated.rawData();
                final String[] row = {
                    raw.timestamp().map(Object::toString).orElse(""),
                    raw.engineRPM().map(Object::toString).orElse(""),
                    raw.engineTemperature().map(Object::toString).orElse(""),
                    raw.rollerRPM().map(Object::toString).orElse(""),
                    raw.torque().map(Object::toString).orElse(""),
                    raw.vehicleSpeed().map(Object::toString).orElse(""),
                    raw.throttlePosition().map(Object::toString).orElse(""),
                    raw.baroPressure().map(Object::toString).orElse(""),
                    raw.exhaustGasTemperature().map(Object::toString).orElse(""),
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
        try (CSVReader reader = new CSVReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.skip(1); // Skip the header row.

            // Continues reading until end of file.
            while (true) {
                final String[] fields = reader.readNext();
                if (fields == null) {
                    break; // End of file reached.
                }

                if (fields.length < HEADER.length) {
                    continue; // Skip malformed lines.
                }

                // Creates a RawData object from the rawdata fields.
                final RawData rawData = RawData.builder()
                    .timestamp(parseOptional(fields[INDEX_TIMESTAMP], Instant::parse))
                    .engineRPM(parseOptional(fields[INDEX_ENGINE_RPM], Integer::parseInt))
                    .engineTemperature(parseOptional(fields[INDEX_ENGINE_TEMPERATURE], Double::parseDouble))
                    .rollerRPM(parseOptional(fields[INDEX_ROLLER_RPM], Integer::parseInt))
                    .torque(parseOptional(fields[INDEX_TORQUE], Double::parseDouble))
                    .vehicleSpeed(parseOptional(fields[INDEX_VEHICLE_SPEED], Integer::parseInt))
                    .throttlePosition(parseOptional(fields[INDEX_THROTTLE_POSITION], Double::parseDouble))
                    .boostPressure(parseOptional(fields[INDEX_BOOST_PRESSURE], Double::parseDouble))
                    .exhaustGasTemperature(parseOptional(fields[INDEX_EXHAUST_GAS_TEMPERATURE], Double::parseDouble))
                    .build();

                // Parses the elaborateddata fields.
                final double powerKW = Double.parseDouble(fields[INDEX_ENGINE_POWER_KW]);
                final double powerHP = Double.parseDouble(fields[INDEX_ENGINE_POWER_HP]);
                final double correctedTorque = Double.parseDouble(fields[INDEX_ENGINE_CORRECTED_TORQUE]);

                importedData.add(new ElaboratedData(rawData, powerKW, powerHP, correctedTorque));
            }
        } catch (final CsvException e) {
            throw new IOException("Error reading CSV file", e);
        }
        return importedData;
    }

    /**
     * A helper method to safely parse a string from the CSV into an Optional.
     *
     * @param <T> The target type of the parsed value.
     * @param value The string value to parse.
     * @param parser A function that can parse the string into the target type.
     * @return An Optional containing the parsed value, or an empty Optional if the
     *         string is null, empty, or a parsing error occurs.
     */
    private <T> Optional<T> parseOptional(final String value, final Function<String, T> parser) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(parser.apply(value));
        } catch (final IllegalArgumentException e) {
            // Catching a more specific exception for parsing errors (e.g., NumberFormatException).
            return Optional.empty();
        }
    }
}

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
 */
public class CsvStrategy implements FileStrategy {

    //Defines the header of the CSV file
     private static final String[] HEADER = {
        // RawData fields
        "timestamp", "engineRPM", "engineTemperature", "rollerRPM", "torque",
        "vehicleSpeed", "throttlePosition", "boostPressure", "exhaustGasTemperature",
        // ElaboratedData fields
        "enginePowerKW", "enginePowerHP", "engineCorrectedTorque"
    };

    /**
     * {@inheritDoc}
     */    
    @Override
    public void exportData(java.util.List<it.unibo.javadyno.model.data.api.ElaboratedData> data, java.io.File file) throws java.io.IOException {
       try (CSVWriter writer = new CSVWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
        writer.writeNext(HEADER); // Writes the header as the first row

        // Writes each field of ElavoratedData (and its nested RawData) as a row in the CSV file
        for(final ElaboratedData elaborated : data) {
            final RawData raw = elaborated.rawData();
                final String[] row = new String[]{
                    // RawData fields
                    // Converts the field to string or uses an empty string if the Optional is empty
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
                    String.valueOf(elaborated.engineCorrectedTorque())
                };
                writer.writeNext(row);
        }
       }
    }

    @Override
    public java.util.List<it.unibo.javadyno.model.data.api.ElaboratedData> importData(java.io.File file) throws java.io.IOException {
        // Implementation for importing data from CSV file
        return null; // Placeholder return statement
    }

}

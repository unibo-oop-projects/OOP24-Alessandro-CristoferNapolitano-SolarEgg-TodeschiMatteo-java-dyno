package it.unibo.javadyno.model.filemanager.impl;

import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.filemanager.api.FileStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JsonStrategy.
 * Verifies that the JSON strategy can correctly export and import ElaboratedData objects.
 */
class JsonStrategyTest {

    private FileStrategy jsonStrategy;
    private List<ElaboratedData> testData;

    @TempDir
    Path tempDir; // JUnit 5 provides a temporary directory for each test

    @BeforeEach
    void setUp() {
        jsonStrategy = new JsonStrategy();
        
        // Create test data with a mix of present and empty Optional values
        final RawData rawData1 = RawData.builder()
            .timestamp(Optional.of(Instant.parse("2023-12-25T10:00:00Z")))
            .engineRPM(Optional.of(3000))
            .engineTemperature(Optional.of(90.5))
            .rollerRPM(Optional.of(2800))
            .torque(Optional.of(205.0))
            .vehicleSpeed(Optional.of(120))
            .throttlePosition(Optional.of(75.5))
            .baroPressure(Optional.of(1013))
            .ambientAirTemperature(Optional.of(25))
            .exhaustGasTemperature(Optional.of(650.0))
            .build();

        final RawData rawData2 = RawData.builder()
            .timestamp(Optional.of(Instant.parse("2023-12-25T10:01:00Z")))
            .engineRPM(Optional.of(3500))
            .engineTemperature(Optional.empty()) // Test empty Optional
            .rollerRPM(Optional.of(3200))
            .torque(Optional.empty()) // Test empty Optional
            .vehicleSpeed(Optional.of(140))
            .throttlePosition(Optional.of(80.0))
            .baroPressure(Optional.of(1012))
            .ambientAirTemperature(Optional.empty()) // Test empty Optional
            .exhaustGasTemperature(Optional.of(675.0))
            .build();

        final ElaboratedData elaborated1 = new ElaboratedData(rawData1, 62.83, 84.25, 205.0);
        final ElaboratedData elaborated2 = new ElaboratedData(rawData2, 78.45, 105.2, 220.5);

        testData = List.of(elaborated1, elaborated2);
    }

    @Test
    void testExportAndImportData() throws IOException {
        // Create a temporary file for testing
        final File testFile = tempDir.resolve("test_data.json").toFile();

        // Export the test data
        jsonStrategy.exportData(testData, testFile);

        // Verify the file was created and is not empty
        assertTrue(testFile.exists(), "JSON file should be created");
        assertTrue(testFile.length() > 0, "JSON file should not be empty");

        // Import the data back
        final List<ElaboratedData> importedData = jsonStrategy.importData(testFile);

        // Verify the imported data matches the original
        assertNotNull(importedData, "Imported data should not be null");
        assertEquals(testData.size(), importedData.size(), "Should import the same number of records");

        // Verify each record in detail
        for (int i = 0; i < testData.size(); i++) {
            final ElaboratedData original = testData.get(i);
            final ElaboratedData imported = importedData.get(i);

            // Check ElaboratedData fields
            assertEquals(original.enginePowerKW(), imported.enginePowerKW(), 0.001, 
                "Engine power KW should match");
            assertEquals(original.enginePowerHP(), imported.enginePowerHP(), 0.001, 
                "Engine power HP should match");
            assertEquals(original.engineCorrectedTorque(), imported.engineCorrectedTorque(), 0.001, 
                "Corrected torque should match");

            // Check RawData fields (including Optional handling)
            final RawData originalRaw = original.rawData();
            final RawData importedRaw = imported.rawData();

            assertEquals(originalRaw.timestamp(), importedRaw.timestamp(), "Timestamps should match");
            assertEquals(originalRaw.engineRPM(), importedRaw.engineRPM(), "Engine RPM should match");
            assertEquals(originalRaw.engineTemperature(), importedRaw.engineTemperature(), 
                "Engine temperature should match");
            assertEquals(originalRaw.rollerRPM(), importedRaw.rollerRPM(), "Roller RPM should match");
            assertEquals(originalRaw.torque(), importedRaw.torque(), "Torque should match");
            assertEquals(originalRaw.vehicleSpeed(), importedRaw.vehicleSpeed(), "Vehicle speed should match");
            assertEquals(originalRaw.throttlePosition(), importedRaw.throttlePosition(), 
                "Throttle position should match");
            assertEquals(originalRaw.baroPressure(), importedRaw.baroPressure(), 
                "Barometric pressure should match");
            assertEquals(originalRaw.ambientAirTemperature(), importedRaw.ambientAirTemperature(), 
                "Ambient air temperature should match");
            assertEquals(originalRaw.exhaustGasTemperature(), importedRaw.exhaustGasTemperature(), 
                "Exhaust gas temperature should match");
        }
    }

    @Test
    void testExportNullDataThrowsException() {
        final File testFile = tempDir.resolve("null_test.json").toFile();
        
        // Should throw an exception when trying to export null data
        assertThrows(NullPointerException.class, () -> {
            jsonStrategy.exportData(null, testFile);
        }, "Should throw NullPointerException for null data");
    }

    @Test
    void testImportEmptyList() throws IOException {
        final File testFile = tempDir.resolve("empty_test.json").toFile();
        
        // Export an empty list
        jsonStrategy.exportData(List.of(), testFile);
        
        // Import it back
        final List<ElaboratedData> importedData = jsonStrategy.importData(testFile);
        
        // Should get an empty list back
        assertNotNull(importedData, "Imported data should not be null");
        assertTrue(importedData.isEmpty(), "Imported data should be empty");
    }

    @Test
    void testImportMalformedJsonThrowsIOException() throws IOException {
        final File testFile = tempDir.resolve("malformed.json").toFile();
        
        // Create a file with malformed JSON
        try (var writer = new java.io.FileWriter(testFile)) {
            writer.write("{ this is not valid json [");
        }
        
        // Should throw IOException when trying to import malformed JSON
        assertThrows(IOException.class, () -> {
            jsonStrategy.importData(testFile);
        }, "Should throw IOException for malformed JSON");
    }

    @Test
    void testImportNonExistentFileThrowsIOException() {
        final File nonExistentFile = tempDir.resolve("does_not_exist.json").toFile();
        
        // Should throw IOException when trying to import from a non-existent file
        assertThrows(IOException.class, () -> {
            jsonStrategy.importData(nonExistentFile);
        }, "Should throw IOException for non-existent file");
    }
}
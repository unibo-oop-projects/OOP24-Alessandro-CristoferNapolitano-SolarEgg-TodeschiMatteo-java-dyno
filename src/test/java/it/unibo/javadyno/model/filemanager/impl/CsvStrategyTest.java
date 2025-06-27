package it.unibo.javadyno.model.filemanager.impl;

import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.RawData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the CsvStrategy class.
 */
final class CsvStrategyTest {

    private CsvStrategy strategy;

    // @TempDir provides a temporary directory for file I/O operations during tests.
    // This directory and its contents are automatically deleted after the test runs.
    @TempDir
    private File tempDir;

    @BeforeEach
    void setUp() {
        this.strategy = new CsvStrategy();
    }

    @Test
    void testExportAndImportRoundTrip() throws IOException {
        // 1. Arrange: Create sample data and a temporary file path.
        final List<ElaboratedData> originalData = createSampleData();
        final File testFile = new File(tempDir, "test_data.csv");

        // 2. Act: Export the data, then import it back.
        this.strategy.exportData(originalData, testFile);
        final List<ElaboratedData> importedData = this.strategy.importData(testFile);

        // 3. Assert: Verify that the imported data matches the original data.
        assertNotNull(importedData);
        assertEquals(originalData.size(), importedData.size());
        // The record's default equals() method provides a deep comparison.
        assertEquals(originalData, importedData);
    }

    @Test
    void testExportEmptyList() throws IOException {
        // 1. Arrange: Create an empty list and a temporary file path.
        final List<ElaboratedData> emptyList = List.of();
        final File testFile = new File(tempDir, "empty_data.csv");

        // 2. Act: Export the empty list.
        this.strategy.exportData(emptyList, testFile);

        // 3. Assert: The file should exist and not be empty (it should contain the header).
        assertTrue(testFile.exists());
        assertTrue(testFile.length() > 0);

        // Optional: Verify that importing from this file results in an empty list.
        final List<ElaboratedData> importedData = this.strategy.importData(testFile);
        assertNotNull(importedData);
        assertTrue(importedData.isEmpty());
    }

    /**
     * Helper method to generate a list of sample data for testing.
     * @return A list containing sample ElaboratedData objects.
     */
    private List<ElaboratedData> createSampleData() {
        // A data point with all fields present.
        final RawData raw1 = RawData.builder()
            .timestamp(Optional.of(Instant.parse("2025-01-10T10:00:00Z")))
            .engineRPM(Optional.of(3000))
            .engineTemperature(Optional.of(90.5))
            .rollerRPM(Optional.of(1500))
            .torque(Optional.of(200.0))
            .vehicleSpeed(Optional.of(100))
            .throttlePosition(Optional.of(75.5))
            .boostPressure(Optional.of(1.2))
            .exhaustGasTemperature(Optional.of(800.0))
            .build();
        final ElaboratedData elaborated1 = new ElaboratedData(raw1, 62.83, 84.25, 205.0);

        // A data point with some optional fields missing.
        final RawData raw2 = RawData.builder()
            .timestamp(Optional.of(Instant.parse("2025-01-10T10:00:01Z")))
            .engineRPM(Optional.of(3100))
            .engineTemperature(Optional.of(91.0))
            .rollerRPM(Optional.of(1550))
            .torque(Optional.of(210.0))
            .vehicleSpeed(Optional.empty()) // Missing data
            .throttlePosition(Optional.empty()) // Missing data
            .boostPressure(Optional.of(1.25))
            .exhaustGasTemperature(Optional.of(810.0))
            .build();
        final ElaboratedData elaborated2 = new ElaboratedData(raw2, 67.9, 91.0, 215.0);

        return List.of(elaborated1, elaborated2);
    }
}
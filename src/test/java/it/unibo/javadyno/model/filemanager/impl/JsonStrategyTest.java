package it.unibo.javadyno.model.filemanager.impl;

import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.filemanager.api.FileStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for JsonStrategy.
 * Verifies that the JSON strategy can correctly export and import ElaboratedData objects.
 */
final class JsonStrategyTest {

    // Test constants to avoid magic numbers
    private static final int TEST_ENGINE_RPM_1 = 3000;
    private static final double TEST_ENGINE_TEMP_1 = 90.5;
    private static final int TEST_ROLLER_RPM_1 = 2800;
    private static final double TEST_TORQUE_1 = 205.0;
    private static final int TEST_VEHICLE_SPEED_1 = 120;
    private static final double TEST_THROTTLE_POS_1 = 75.5;
    private static final int TEST_BARO_PRESSURE_1 = 1013;
    private static final int TEST_AMBIENT_TEMP_1 = 25;
    private static final double TEST_EXHAUST_TEMP_1 = 650.0;
    private static final double TEST_PWR_KW_1 = 62.83;
    private static final double TEST_POWER_HP_1 = 84.25;
    private static final double TEST_CORRECTED_TORQUE_1 = 205.0;

    private static final int TEST_ENGINE_RPM_2 = 3500;
    private static final int TEST_ROLLER_RPM_2 = 3200;
    private static final int TEST_VEHICLE_SPEED_2 = 140;
    private static final double TEST_THROTTLE_POS_2 = 80.0;
    private static final int TEST_BARO_PRESSURE_2 = 1012;
    private static final double TEST_EXHAUST_TEMP_2 = 675.0;
    private static final double TEST_PWR_KW_2 = 78.45;
    private static final double TEST_POWER_HP_2 = 105.2;
    private static final double TEST_CORRECTED_TORQUE_2 = 220.5;

    private static final double DELTA = 0.001;

    private FileStrategy jsonStrategy;
    private List<ElaboratedData> testData;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        jsonStrategy = new JsonStrategy();
        testData = createTestData();
    }

    @Test
    void testExportAndImportData() throws IOException {
        final File testFile = tempDir.resolve("test_data.json").toFile();

        // Exports and imports the data.
        jsonStrategy.exportData(testData, testFile);
        final List<ElaboratedData> importedData = jsonStrategy.importData(testFile);

        // Verifies results.
        assertTrue(testFile.exists());
        assertTrue(testFile.length() > 0);
        assertNotNull(importedData);
        assertEquals(testData.size(), importedData.size());
        verifyDataEquality(testData, importedData);
    }

    @Test
    void testExportNullDataThrowsException() {
        final File testFile = tempDir.resolve("null_test.json").toFile();

        assertThrows(NullPointerException.class, () -> {
            jsonStrategy.exportData(null, testFile);
        });
    }

    @Test
    void testImportEmptyList() throws IOException {
        final File testFile = tempDir.resolve("empty_test.json").toFile();

        jsonStrategy.exportData(List.of(), testFile);
        final List<ElaboratedData> importedData = jsonStrategy.importData(testFile);

        assertNotNull(importedData);
        assertTrue(importedData.isEmpty());
    }

    @Test
    void testImportMalformedJsonThrowsIOException() throws IOException {
        final File testFile = tempDir.resolve("malformed.json").toFile();

        try (FileWriter writer = new FileWriter(testFile, StandardCharsets.UTF_8)) {
            writer.write("{ this is not valid json [");
        }

        assertThrows(IOException.class, () -> {
            jsonStrategy.importData(testFile);
        });
    }

    @Test
    void testImportNonExistentFileThrowsIOException() {
        final File nonExistentFile = tempDir.resolve("does_not_exist.json").toFile();

        assertThrows(IOException.class, () -> {
            jsonStrategy.importData(nonExistentFile);
        });
    }

    private List<ElaboratedData> createTestData() {
        // First record with all data present.
        final RawData rawData1 = RawData.builder()
            .timestamp(Optional.of(Instant.parse("2023-12-25T10:00:00Z")))
            .engineRPM(Optional.of(TEST_ENGINE_RPM_1))
            .engineTemperature(Optional.of(TEST_ENGINE_TEMP_1))
            .rollerRPM(Optional.of(TEST_ROLLER_RPM_1))
            .torque(Optional.of(TEST_TORQUE_1))
            .vehicleSpeed(Optional.of(TEST_VEHICLE_SPEED_1))
            .throttlePosition(Optional.of(TEST_THROTTLE_POS_1))
            .baroPressure(Optional.of(TEST_BARO_PRESSURE_1))
            .ambientAirTemperature(Optional.of(TEST_AMBIENT_TEMP_1))
            .exhaustGasTemperature(Optional.of(TEST_EXHAUST_TEMP_1))
            .build();

        // Second record point with some empty fields.
        final RawData rawData2 = RawData.builder()
            .timestamp(Optional.of(Instant.parse("2023-12-25T10:01:00Z")))
            .engineRPM(Optional.of(TEST_ENGINE_RPM_2))
            .engineTemperature(Optional.empty())
            .rollerRPM(Optional.of(TEST_ROLLER_RPM_2))
            .torque(Optional.empty())
            .vehicleSpeed(Optional.of(TEST_VEHICLE_SPEED_2))
            .throttlePosition(Optional.of(TEST_THROTTLE_POS_2))
            .baroPressure(Optional.of(TEST_BARO_PRESSURE_2))
            .ambientAirTemperature(Optional.empty())
            .exhaustGasTemperature(Optional.of(TEST_EXHAUST_TEMP_2))
            .build();

        final ElaboratedData elaborated1 = new ElaboratedData(rawData1, TEST_PWR_KW_1, TEST_POWER_HP_1, TEST_CORRECTED_TORQUE_1);
        final ElaboratedData elaborated2 = new ElaboratedData(rawData2, TEST_PWR_KW_2, TEST_POWER_HP_2, TEST_CORRECTED_TORQUE_2);

        return List.of(elaborated1, elaborated2);
    }

    private void verifyDataEquality(final List<ElaboratedData> original, final List<ElaboratedData> imported) {
        for (int i = 0; i < original.size(); i++) {
            final ElaboratedData orig = original.get(i);
            final ElaboratedData imp = imported.get(i);

            // Check elaborated data fields
            assertEquals(orig.enginePowerKW(), imp.enginePowerKW(), DELTA);
            assertEquals(orig.enginePowerHP(), imp.enginePowerHP(), DELTA);
            assertEquals(orig.engineCorrectedTorque(), imp.engineCorrectedTorque(), DELTA);

            // Check raw data fields
            final RawData origRaw = orig.rawData();
            final RawData impRaw = imp.rawData();

            assertEquals(origRaw.timestamp(), impRaw.timestamp());
            assertEquals(origRaw.engineRPM(), impRaw.engineRPM());
            assertEquals(origRaw.engineTemperature(), impRaw.engineTemperature());
            assertEquals(origRaw.rollerRPM(), impRaw.rollerRPM());
            assertEquals(origRaw.torque(), impRaw.torque());
            assertEquals(origRaw.vehicleSpeed(), impRaw.vehicleSpeed());
            assertEquals(origRaw.throttlePosition(), impRaw.throttlePosition());
            assertEquals(origRaw.baroPressure(), impRaw.baroPressure());
            assertEquals(origRaw.ambientAirTemperature(), impRaw.ambientAirTemperature());
            assertEquals(origRaw.exhaustGasTemperature(), impRaw.exhaustGasTemperature());
        }
    }
}

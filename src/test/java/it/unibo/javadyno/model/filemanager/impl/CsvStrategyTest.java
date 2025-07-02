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

    // Test constants to avoid magic numbers.
    private static final int TEST_ENGINE_RPM_1 = 3000;
    private static final double TEST_ENGINE_TEMP_1 = 90.5;
    private static final int TEST_ROLLER_RPM_1 = 1500;
    private static final double TEST_TORQUE_1 = 200.0;
    private static final int TEST_VEHICLE_SPEED_1 = 100;
    private static final double TEST_THROTTLE_POS_1 = 75.5;
    private static final int TEST_BARO_PRESSURE_1 = 1013;
    private static final int TEST_AMBIENT_TEMP_1 = 25;
    private static final double TEST_EXHAUST_TEMP_1 = 800.0;
    private static final double TEST_POWER_KW_1 = 62.83;
    private static final double TEST_POWER_HP_1 = 84.25;
    private static final double TEST_CORRECTED_TORQUE_1 = 205.0;
    private static final int TEST_AMBIENT_HUMIDITY_1 = 60;

    private static final int TEST_ENGINE_RPM_2 = 3100;
    private static final double TEST_ENGINE_TEMP_2 = 91.0;
    private static final int TEST_ROLLER_RPM_2 = 1550;
    private static final double TEST_TORQUE_2 = 210.0;
    private static final int TEST_BARO_PRESSURE_2 = 1012;
    private static final double TEST_EXHAUST_TEMP_2 = 810.0;
    private static final double TEST_POWER_KW_2 = 67.9;
    private static final double TEST_POWER_HP_2 = 91.0;
    private static final double TEST_CORRECTED_TORQUE_2 = 215.0;
    private static final int TEST_AMBIENT_HUMIDITY_2 = 65;

    private CsvStrategy strategy;

    @TempDir
    private File tempDir;

    @BeforeEach
    void setUp() {
        this.strategy = new CsvStrategy();
    }

    @Test
    void testExportAndImportRoundTrip() throws IOException {
        final List<ElaboratedData> originalData = createSampleData();
        final File testFile = new File(tempDir, "test_data.csv");

        this.strategy.exportData(originalData, testFile);
        final List<ElaboratedData> importedData = this.strategy.importData(testFile);

        assertNotNull(importedData);
        assertEquals(originalData.size(), importedData.size());
        assertEquals(originalData, importedData);
    }

    @Test
    void testExportEmptyList() throws IOException {
        final List<ElaboratedData> emptyList = List.of();
        final File testFile = new File(tempDir, "empty_data.csv");

        this.strategy.exportData(emptyList, testFile);

        assertTrue(testFile.exists());
        assertTrue(testFile.length() > 0);

        final List<ElaboratedData> importedData = this.strategy.importData(testFile);
        assertNotNull(importedData);
        assertTrue(importedData.isEmpty());
    }

    /**
     * Helper method to generate sample test data.
     *
     * @return A list containing sample ElaboratedData objects.
     */
    private List<ElaboratedData> createSampleData() {
        final RawData raw1 = RawData.builder()
            .timestamp(Optional.of(Instant.parse("2025-01-10T10:00:00Z")))
            .engineRPM(Optional.of(TEST_ENGINE_RPM_1))
            .engineTemperature(Optional.of(TEST_ENGINE_TEMP_1))
            .rollerRPM(Optional.of(TEST_ROLLER_RPM_1))
            .torque(Optional.of(TEST_TORQUE_1))
            .vehicleSpeed(Optional.of(TEST_VEHICLE_SPEED_1))
            .throttlePosition(Optional.of(TEST_THROTTLE_POS_1))
            .baroPressure(Optional.of(TEST_BARO_PRESSURE_1))
            .ambientAirTemperature(Optional.of(TEST_AMBIENT_TEMP_1))
            .ambientHumidity(Optional.of(TEST_AMBIENT_HUMIDITY_1))
            .exhaustGasTemperature(Optional.of(TEST_EXHAUST_TEMP_1))
            .build();
        final ElaboratedData elaborated1 = new ElaboratedData(raw1, TEST_POWER_KW_1, TEST_POWER_HP_1, TEST_CORRECTED_TORQUE_1);

        final RawData raw2 = RawData.builder()
            .timestamp(Optional.of(Instant.parse("2025-01-10T10:00:01Z")))
            .engineRPM(Optional.of(TEST_ENGINE_RPM_2))
            .engineTemperature(Optional.of(TEST_ENGINE_TEMP_2))
            .rollerRPM(Optional.of(TEST_ROLLER_RPM_2))
            .torque(Optional.of(TEST_TORQUE_2))
            .vehicleSpeed(Optional.empty())
            .throttlePosition(Optional.empty())
            .baroPressure(Optional.of(TEST_BARO_PRESSURE_2))
            .ambientAirTemperature(Optional.empty())
            .ambientHumidity(Optional.of(TEST_AMBIENT_HUMIDITY_2))
            .exhaustGasTemperature(Optional.of(TEST_EXHAUST_TEMP_2))
            .build();
        final ElaboratedData elaborated2 = new ElaboratedData(raw2, TEST_POWER_KW_2, TEST_POWER_HP_2, TEST_CORRECTED_TORQUE_2);

        return List.of(elaborated1, elaborated2);
    }
}

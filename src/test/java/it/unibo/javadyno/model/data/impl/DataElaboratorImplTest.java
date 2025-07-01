package it.unibo.javadyno.model.data.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.api.UserSettings;
import it.unibo.javadyno.model.dyno.api.Dyno;

class DataElaboratorImplTest {

    private static final int DATA_NUMBER = 50;
    private final Random rand = new Random();
    private DataElaboratorImpl dataElaborator;

    @BeforeEach
    void setUp() {
        this.dataElaborator = new DataElaboratorImpl(new TestOBD2Dyno(), new UserSettings());
    }

    @Test
    void testRecievedData() {
        assertNull(dataElaborator.getElaboratedData());
        final ElaboratedData elaboratedData = dataElaborator.getElaboratedData();
        assertNotNull(elaboratedData);
        assertNotNull(elaboratedData.engineCorrectedTorque());
        assertNotNull(elaboratedData.enginePowerHP());
        assertNotNull(elaboratedData.enginePowerKW());
    }

    @Test
    void testRealisticCalculation() {
        assertNull(dataElaborator.getElaboratedData());
        final ElaboratedData prevElaboratedData = dataElaborator.getElaboratedData();
        for (int i = 0; i < DATA_NUMBER; i++) {
            final ElaboratedData elaboratedData = dataElaborator.getElaboratedData();
            assertTrue(() -> prevElaboratedData.enginePowerKW() < elaboratedData.enginePowerKW());
            assertTrue(() -> prevElaboratedData.enginePowerHP() < elaboratedData.enginePowerHP());
        }
    }

    private final class TestOBD2Dyno implements Dyno {

        // Initial values
        private static final Integer INITIAL_ENGINE_RPM = 500;
        private static final Integer INITIAL_VEHICLE_SPEED = 2;
        private static final Integer INITIAL_AMBIENT_TEMPERATURE = 20;
        private static final Integer INITIAL_BARO_PRESSURE = 101;

        // Engine RPM constants
        private static final double RPM_INCREASE_FACTOR = 1.05;
        private static final int MAX_ENGINE_RPM = 7000;

        // Vehicle speed constants
        private static final int MAX_VEHICLE_SPEED = 180;
        private static final int MAX_SPEED_INCREASE = 8;
        private static final int MIN_SPEED_INCREASE = 1;

        // Timestamp constants
        private static final int MIN_DELAY_MILLIS = 800;
        private static final int MAX_DELAY_MILLIS = 1100;
        private RawData prevRawData;

        TestOBD2Dyno() {
            this.prevRawData = RawData.builder()
                    .engineRPM(Optional.of(INITIAL_ENGINE_RPM))
                    .vehicleSpeed(Optional.of(INITIAL_VEHICLE_SPEED))
                    .ambientAirTemperature(Optional.of(INITIAL_AMBIENT_TEMPERATURE))
                    .baroPressure(Optional.of(INITIAL_BARO_PRESSURE))
                    .timestamp(Optional.of(Instant.now()))
                    .build();
        }

        @Override
        public RawData getRawData() {
            // Calculate dynamic engine RPM - increase by percentage each call, with max limit
            final int currentRpm = this.prevRawData.engineRPM().get();
            final Integer newRpm = Math.min(MAX_ENGINE_RPM, (int) (currentRpm * RPM_INCREASE_FACTOR));

            // Calculate dynamic vehicle speed - logarithmic growth for realistic acceleration
            // Fast initial acceleration that slows down as it approaches max speed
            final int currentSpeed = this.prevRawData.vehicleSpeed().get();
            final double accelerationFactor = 1.0 - (double) currentSpeed / MAX_VEHICLE_SPEED;
            final int speedIncrease = Math.max(MIN_SPEED_INCREASE, (int) (MAX_SPEED_INCREASE * accelerationFactor));
            final Integer newSpeed = Math.min(MAX_VEHICLE_SPEED, currentSpeed + speedIncrease);

            // Keep ambient temperature and barometric pressure constant
            final Integer ambientTemp = this.prevRawData.ambientAirTemperature().get();
            final Integer baroPressure = this.prevRawData.baroPressure().get();

            // Generate realistic timestamp with variable delay between measurements
            final Instant prevTimestamp = this.prevRawData.timestamp().get();
            final int delayMillis = (int) (MIN_DELAY_MILLIS
                + rand.nextDouble()
                * (MAX_DELAY_MILLIS - MIN_DELAY_MILLIS));
            final Instant newTimestamp = prevTimestamp.plusMillis(delayMillis);

            final RawData rawData = RawData.builder()
                    .engineRPM(Optional.of(newRpm))
                    .vehicleSpeed(Optional.of(newSpeed))
                    .ambientAirTemperature(Optional.of(ambientTemp))
                    .baroPressure(Optional.of(baroPressure))
                    .timestamp(Optional.of(newTimestamp))
                    .build();
            this.prevRawData = rawData;
            return rawData;
        }

        @Override
        public DataSource getDynoType() {
            return DataSource.OBD2;
        }

        @Override
        public void begin() {
        }

        @Override
        public void end() {
        }

        @Override
        public boolean isActive() {
            return true;
        }
    }
}

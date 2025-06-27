package it.unibo.javadyno.model.data.impl;

import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.dyno.api.Dyno;

class DataElaboratorTest {

    private DataElaboratorImpl dataElaborator;

    @BeforeAll
    void setUp() {
        this.dataElaborator = new DataElaboratorImpl(new TestOBD2Dyno());
    }

    private final class TestOBD2Dyno implements Dyno {

        private RawData prevRawData;

        public TestOBD2Dyno() {
            this.prevRawData = RawData.builder()
                    .engineRPM(Optional.of(500))
                    .vehicleSpeed(Optional.of(2))
                    .ambientAirTemperature(Optional.of(20))
                    .baroPressure(Optional.of(101))
                    .build();
        }

        @Override
        public RawData getRawData() {
            final RawData rawData = RawData.builder()
                    .engineRPM(Optional.of(this.prevRawData.engineRPM().get()))
                    .vehicleSpeed(Optional.of(this.prevRawData.vehicleSpeed().get()))
                    .ambientAirTemperature(Optional.of(this.prevRawData.ambientAirTemperature().get()))
                    .baroPressure(Optional.of(this.prevRawData.baroPressure().get()))
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

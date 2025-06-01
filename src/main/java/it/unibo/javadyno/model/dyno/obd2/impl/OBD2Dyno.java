package it.unibo.javadyno.model.dyno.obd2.impl;

import java.time.Instant;
import java.util.Optional;
import it.unibo.javadyno.model.data.communicator.api.JsonScheme;
import org.json.JSONException;
import org.json.JSONObject;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.dyno.impl.AbstractPhysicalDyno;

/**
 * Implementation of the Dyno interface.
 * It retrieve engine RPM, vehicle speed, and other vehicle data from the OBD2 line
 * packed in a RawData object.
 */
public class OBD2Dyno extends AbstractPhysicalDyno {

    private Optional<Integer> engineRpm;
    private Optional<Integer> vehicleSpeed;
    private Optional<Double> engineTemperature;
    private Optional<Instant> timestamp;

    /**
     * Initializes the OBD2Dyno with default values.
     */
    public OBD2Dyno(final MCUCommunicator communicator) {
        super(communicator);
        this.engineRpm = Optional.empty();
        this.vehicleSpeed = Optional.empty();
        this.engineTemperature = Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RawData getRawData() {
        return RawData.builder()
                .engineRPM(this.engineRpm)
                .vehicleSpeed(this.vehicleSpeed)
                .engineTemperature(this.engineTemperature)
                .timestamp(this.timestamp)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSource getDynoType() {
        return DataSource.OBD2;
    }

    /**
     * {@inheritDoc}
     */
    protected void handleMessage(final String message) {
        try {
            final var json = new JSONObject(message);
            this.engineRpm = json.has(JsonScheme.ENGINE_RPM.getActualName())
                ? Optional.of(json.getInt(JsonScheme.ENGINE_RPM.getActualName()))
                : Optional.empty();

            this.vehicleSpeed = json.has(JsonScheme.VEHICLE_SPEED.getActualName())
                ? Optional.of(json.getInt(JsonScheme.VEHICLE_SPEED.getActualName()))
                : Optional.empty();

            this.engineTemperature = json.has(JsonScheme.ENGINE_TEMPERATURE.getActualName())
                ? Optional.of(json.getDouble(JsonScheme.ENGINE_TEMPERATURE.getActualName()))
                : Optional.empty();
            
            this.timestamp = json.has(JsonScheme.TIMESTAMP.getActualName())
                ? Optional.of(Instant.parse(json.getString(JsonScheme.TIMESTAMP.getActualName())))
                : Optional.empty();
        } catch (final JSONException e) {
            // Tell alert monitor
        }
    }
}

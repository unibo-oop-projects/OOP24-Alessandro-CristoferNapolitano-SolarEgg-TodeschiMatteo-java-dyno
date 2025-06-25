package it.unibo.javadyno.model.dyno.real.impl;

import java.time.Instant;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.communicator.api.JsonScheme;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.dyno.impl.AbstractPhysicalDyno;
import it.unibo.javadyno.model.dyno.real.api.RealDyno;

/**
 * Implementation of the RealDyno interface.
 * This class extends AbstractPhysicalDyno and provides methods to interact with a real dynamometer.
 */
public class RealDynoImpl extends AbstractPhysicalDyno implements RealDyno {

    private Optional<Integer> engineRpm;
    private Optional<Double> engineTemperature;
    private Optional<Double> torque;
    private Optional<Double> throttlePosition;
    private Optional<Instant> timestamp;

    /**
     * Initializes the ReadlDynoImpl with the given MCUCommunicator.
     *
     * @param communicator the MCUCommunicator to use for communication.
     */
    public RealDynoImpl(final MCUCommunicator communicator) {
        super(communicator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendConfiguration(final String config) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendConfiguration'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RawData getRawData() {
        return RawData.builder()
                .engineRPM(this.engineRpm)
                .engineTemperature(this.engineTemperature)
                .torque(this.torque)
                .throttlePosition(this.throttlePosition)
                .timestamp(this.timestamp)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSource getDynoType() {
        return DataSource.REAL_DYNO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleMessage(final String message) {
        try {
            final var json = new JSONObject(message);
            this.engineRpm = json.has(JsonScheme.ENGINE_RPM.getActualName())
                ? Optional.of(json.getInt(JsonScheme.ENGINE_RPM.getActualName()))
                : Optional.empty();

            this.engineTemperature = json.has(JsonScheme.ENGINE_TEMPERATURE.getActualName())
                ? Optional.of(json.getDouble(JsonScheme.ENGINE_TEMPERATURE.getActualName()))
                : Optional.empty();

            this.torque = json.has(JsonScheme.TORQUE.getActualName())
                ? Optional.of(json.getDouble(JsonScheme.TORQUE.getActualName()))
                : Optional.empty();

            this.throttlePosition = json.has(JsonScheme.THROTTLE_POSITION.getActualName())
                ? Optional.of(json.getDouble(JsonScheme.THROTTLE_POSITION.getActualName()))
                : Optional.empty();

            this.timestamp = json.has(JsonScheme.TIMESTAMP.getActualName())
                ? Optional.of(Instant.parse(json.getString(JsonScheme.TIMESTAMP.getActualName())))
                : Optional.empty();
        } catch (final JSONException e) {
            // Tell alert monitor
            throw new IllegalArgumentException("Invalid JSON message received: " + message, e);
        }
    }

}

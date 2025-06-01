package it.unibo.javadyno.model.dyno.obd2.impl;

import java.util.Optional;
import it.unibo.javadyno.model.data.communicator.api.JsonScheme;
import org.json.JSONException;
import org.json.JSONObject;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.data.communicator.impl.WebSocketMCUCommunicator;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Implementation of the Dyno interface.
 * It retrieve engine RPM, vehicle speed, and other vehicle data from the OBD2 line
 * packed in a RawData object.
 */
public class OBD2Dyno implements Dyno {

    private final MCUCommunicator communicator;
    private volatile boolean active;
    private Optional<Integer> engineRpm;
    private Optional<Integer> vehicleSpeed;
    private Optional<Double> engineTemperature;

    /**
     * Initializes the OBD2Dyno with default values.
     */
    public OBD2Dyno() {
        this.communicator = new WebSocketMCUCommunicator();
        this.active = false;
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
    @Override
    public void begin() {
        if (!this.isActive()) {
            try {
                this.communicator.connect();
                this.active = true;
                this.communicator.addMessageListener(this::messageHandler);
            } catch (final InterruptedException e) {
                // Tell alert monitor
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end() {
        if (this.isActive()) {
            try {
                this.communicator.disconnect();
                this.active = false;
                this.communicator.removeMessageListener(this::messageHandler);
            } catch (final InterruptedException e) {
                // Tell alert monitor
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        return this.active;
    }

    /**
     * Handles incoming messages from the {@code MCUCommunicator}.
     * Parses the JSON message and updates the data.
     *
     * @param message the JSON message received from the communicator
     */
    private void messageHandler(final String message) {
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
        } catch (final JSONException e) {
            // Tell alert monitor
        }
    }
}

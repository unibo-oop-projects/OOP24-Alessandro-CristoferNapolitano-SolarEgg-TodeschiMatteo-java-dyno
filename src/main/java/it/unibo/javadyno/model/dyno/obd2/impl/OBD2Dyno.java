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
public class OBD2Dyno implements Dyno, Runnable {

    private static final int MAX_ENGINE_RPM = 8000;
    private static final int MAX_VEHICLE_SPEED = 200;
    private static final double MIN_ENGINE_TEMP = 20.0;
    private static final double ENGINE_TEMP_RANGE = 80.0;
    private final MCUCommunicator communicator;
    private volatile boolean active;
    private Thread thread;
    private Optional<Integer> engineRpm;
    private Optional<Integer> vehicleSpeed;
    private Optional<Double> engineTemperature;

    /**
     * Initializes the OBD2Dyno with default values.
     */
    public OBD2Dyno() {
        this.communicator = new WebSocketMCUCommunicator();
        this.thread = null;
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
            this.active = true;
            this.thread = Thread.ofVirtual().unstarted(this);
            this.thread.start();

            // connect to the communicator
            this.communicator.addMessageListener(this::messageHandler);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end() {
        this.active = false;
        try {
            this.thread.interrupt();
        } catch (final SecurityException e) {
            // already stopping
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
     * {@inheritDoc}
     */
    @Override
    public void run() {
        while (this.isActive()) {
            // Simulate reading data from the OBD2 line.
            this.engineRpm = Optional.of((int) (Math.random() * MAX_ENGINE_RPM)); // Simulated RPM
            this.vehicleSpeed = Optional.of((int) (Math.random() * MAX_VEHICLE_SPEED)); // Simulated speed
            this.engineTemperature = Optional.of(MIN_ENGINE_TEMP + Math.random() * ENGINE_TEMP_RANGE); // Simulated temperature

            // Actual reading from OBD2 would go here

            try {
                Thread.sleep(100); // Sleep for 100 milliseconds
            } catch (final InterruptedException e) {
                this.end();
                break;
            }
        }
    }

    /**
     * Handles incoming messages from the {@code MCUCommunicator} .
     * Parses the JSON message and updates the dyno data.
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
            // Tell lert monitor
        }
    }
}

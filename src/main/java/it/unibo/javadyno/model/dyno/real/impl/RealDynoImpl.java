package it.unibo.javadyno.model.dyno.real.impl;

import java.time.Instant;
import java.util.Optional;
import org.json.JSONObject;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.communicator.api.JsonScheme;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.dyno.impl.AbstractPhysicalDyno;
import javafx.util.Pair;

/**
 * Implementation of the RealDyno interface.
 * This class extends AbstractPhysicalDyno and provides methods to interact with a real dynamometer.
 */
public final class RealDynoImpl extends AbstractPhysicalDyno<Pair<JsonScheme, Double>> {

    private static final JSONObject CONFIGURATION_MESSAGE = new JSONObject()
        .put("command", "configure")
        .put("parameters", new JSONObject()
            .put("engineRpm", true)
            .put("engineTemperature", true)
            .put("torque", true)
            .put("throttlePosition", true));
    private static final String THREAD_NAME = "RealDyno";
    private boolean mcuConfigured;
    private Optional<Integer> engineRpm = Optional.empty();
    private Optional<Double> engineTemperature = Optional.empty();
    private Optional<Double> torque = Optional.empty();
    private Optional<Double> throttlePosition = Optional.empty();
    private Optional<Instant> timestamp = Optional.empty();

    /**
     * Initializes the ReadlDynoImpl with the given MCUCommunicator.
     *
     * @param communicator the MCUCommunicator to use for communication.
     */
    public RealDynoImpl(final MCUCommunicator<Pair<JsonScheme, Double>> communicator) {
        super(communicator);
    }

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

    @Override
    public DataSource getDynoType() {
        return DataSource.REAL_DYNO;
    }

    /**
     * Processes incoming messages from the real dyno MCU and updates corresponding sensor data.
     * Updates the values based on the JsonScheme key.
     * Sets the current timestamp for each processed message.
     *
     * @param message a Pair containing the JsonScheme type and the sensor value
     */
    @Override
    protected void handleMessage(final Pair<JsonScheme, Double> message) {
        this.timestamp = Optional.of(Instant.now());
        switch (message.getKey()) {
            case ENGINE_RPM -> this.engineRpm = Optional.of(message.getValue().intValue());
            case ENGINE_TEMPERATURE -> this.engineTemperature = Optional.of(message.getValue());
            case TORQUE -> this.torque = Optional.of(message.getValue());
            case THROTTLE_POSITION -> this.throttlePosition = Optional.of(message.getValue());
            default -> throw new IllegalArgumentException("Unknown message type: " + message.getKey());
        }
    }

    /**
     * Returns the configuration message to be sent to the MCU.
     * After the first call, it returns an empty string.
     */
    @Override
    protected String getOutgoingMessage() {
        if (!this.mcuConfigured) {
            this.mcuConfigured = true;
            return CONFIGURATION_MESSAGE.toString();
        }
        return "";
    }

    @Override
    protected String getThreadName() {
        return THREAD_NAME;
    }

}

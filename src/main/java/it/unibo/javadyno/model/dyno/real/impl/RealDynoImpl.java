package it.unibo.javadyno.model.dyno.real.impl;

import java.time.Instant;
import java.util.Optional;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.communicator.api.JsonScheme;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.dyno.impl.AbstractPhysicalDyno;
import it.unibo.javadyno.model.dyno.real.api.RealDyno;
import javafx.util.Pair;

/**
 * Implementation of the RealDyno interface.
 * This class extends AbstractPhysicalDyno and provides methods to interact with a real dynamometer.
 */
public final class RealDynoImpl extends AbstractPhysicalDyno<Pair<JsonScheme, Double>> implements RealDyno {

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
    public RealDynoImpl(final MCUCommunicator<Pair<JsonScheme, Double>> communicator) {
        super(communicator);
    }

    @Override
    public void sendConfiguration(final String config) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendConfiguration'");
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

    @Override
    protected void handleMessage(final Pair<JsonScheme, Double> message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleMessage'");
    }

    @Override
    protected String getOutgoingMessage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOutgoingMessage'");
    }

    @Override
    protected String getThreadName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getThreadName'");
    }
}

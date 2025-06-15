package it.unibo.javadyno.model.dyno.obd2.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.data.communicator.impl.SerialMCUCommunicator;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Implementation of the Dyno interface.
 * It retrieve engine RPM, vehicle speed, and other vehicle data from the OBD2 line
 * and packets them in a RawData object when requested.
 */
public final class OBD2Dyno implements Dyno, Runnable {

    private static final int DEFAULT_POLLING = 100;
    private final MCUCommunicator communicator;
    private volatile boolean active;
    private final int polling;
    private Consumer<String> messageHandler;
    private Optional<Integer> engineRpm;
    private Optional<Integer> vehicleSpeed;
    private Optional<Double> engineTemperature;

    /**
     * Initializes the OBD2Dyno with default values.
     */
    public OBD2Dyno() {
        this(DEFAULT_POLLING);
    }

    /**
     * Initializes the OBD2Dyno with a specified communicator and default polling interval.
     *
     * @param communicator the MCUCommunicator to use for communication
     */
    public OBD2Dyno(final MCUCommunicator communicator) {
        this(communicator, DEFAULT_POLLING);
    }

    /**
     * Initializes the OBD2Dyno with a specified polling interval and a default communicator.
     *
     * @param polling the polling interval in milliseconds
     */
    public OBD2Dyno(final int polling) {
        this(new SerialMCUCommunicator(), polling);
    }

    /**
     * Initializes the OBD2Dyno with a specified communicator and polling interval.
     *
     * @param communicator the MCUCommunicator to use for communication
     * @param polling the polling interval in milliseconds
     */
    public OBD2Dyno(final MCUCommunicator communicator, final int polling) {
        Objects.requireNonNull(communicator, "Communicator cannot be null");
        this.communicator = communicator;
        this.polling = polling;
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
                Thread.ofVirtual()
                    .name("OBD2Dyno-MessageHandler")
                    .start(this);
                this.messageHandler = this::messageHandler;
                this.communicator.addMessageListener(this.messageHandler);
            } catch (final InterruptedException e) {
                // Tell alert monitor
                throw new IllegalStateException("Failed to connect the communicator", e);
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
                this.communicator.removeMessageListener(this.messageHandler);
            } catch (final InterruptedException e) {
                throw new IllegalStateException("Failed to disconnect the communicator", e);
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

    private void messageHandler(final String message) {
        
    }

    @Override
    public void run() {
        while (this.isActive()) {

            try {
                Thread.sleep(this.polling);
            } catch (final InterruptedException e) {
                // Tell alert monitor
                Thread.currentThread().interrupt();
            }
        }
    }
}

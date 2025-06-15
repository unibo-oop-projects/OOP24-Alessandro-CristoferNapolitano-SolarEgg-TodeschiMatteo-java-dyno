package it.unibo.javadyno.model.dyno.obd2.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.data.communicator.impl.SerialMCUCommunicator;
import it.unibo.javadyno.model.dyno.api.Dyno;
import it.unibo.javadyno.model.dyno.obd2.api.Mode;
import it.unibo.javadyno.model.dyno.obd2.api.PID;

/**
 * Implementation of the Dyno interface.
 * It retrieve engine RPM, vehicle speed, and other vehicle data from the OBD2 line
 * and packets them in a RawData object when requested.
 */
public final class OBD2Dyno implements Dyno, Runnable {

    private static final int DEFAULT_POLLING = 100;
    private static final int DATA_NUMERICAL_BASE = 16;
    private static final int HEADER_LENGTH = 4;
    private static final int SEGMENTS_LENGTH = 2;
    private static final int MODE_OFFSET = 40;
    private static final int RPM_MULTIPLIER = 256;
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
            this.active = true;
            Thread.ofVirtual()
                .name("OBD2Dyno-MessageHandler")
                .start(this);
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
        // 01 00 (mode 01, PID 00) -> 41 00 0C 1A (data = 0C1A)
        // first 4 digits are "repeated"
        final String header = message.substring(0, HEADER_LENGTH);
        final int mode = Integer.parseInt(header.substring(0, SEGMENTS_LENGTH)) - MODE_OFFSET;

        if (mode == Mode.CURRENT_DATA.getCode()) {
            final Integer pid = Integer.parseInt(header.substring(SEGMENTS_LENGTH, HEADER_LENGTH), DATA_NUMERICAL_BASE);

            if (pid == PID.ENGINE_RPM.getCode()) {
                // RPM formula: RPM = (A * 256 + B) / 4
                // where A is the first byte and B is the second byte
                final int rpmA = Integer.parseInt(
                    message.substring(HEADER_LENGTH, HEADER_LENGTH + SEGMENTS_LENGTH),
                    DATA_NUMERICAL_BASE
                );
                final int rpmB = Integer.parseInt(message.substring(HEADER_LENGTH + SEGMENTS_LENGTH), DATA_NUMERICAL_BASE);
                this.engineRpm = Optional.of((rpmA * RPM_MULTIPLIER + rpmB) / 4);
            } else if (pid == PID.VEHICLE_SPEED.getCode()) {
                final String speedData = message.substring(HEADER_LENGTH, HEADER_LENGTH + SEGMENTS_LENGTH);
                this.vehicleSpeed = Optional.of(Integer.parseInt(speedData, DATA_NUMERICAL_BASE));
            } else if (pid == PID.ENGINE_COOLANT_TEMPERATURE.getCode()) {
                final String tempData = message.substring(HEADER_LENGTH, HEADER_LENGTH + SEGMENTS_LENGTH);
                this.engineTemperature = Optional.of((double)
                    (Integer.parseInt(tempData, DATA_NUMERICAL_BASE)
                    - PID.ENGINE_COOLANT_TEMPERATURE.getOffset())
                );
            }
        }
    }

    @Override
    public void run() {
        try {
            this.communicator.connect();
        } catch (final InterruptedException e) {
            // Tell alert monitor
            throw new IllegalStateException("Failed to connect the communicator", e);
        }
        this.messageHandler = this::messageHandler;
        this.communicator.addMessageListener(this.messageHandler);
        while (this.isActive()) {
            // send OBD2 commands
            try {
                Thread.sleep(this.polling);
            } catch (final InterruptedException e) {
                // Tell alert monitor
                Thread.currentThread().interrupt();
            }
        }
    }
}

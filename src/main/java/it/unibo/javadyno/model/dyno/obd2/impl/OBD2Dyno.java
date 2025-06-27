package it.unibo.javadyno.model.dyno.obd2.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import org.apache.commons.collections4.iterators.LoopingIterator;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.data.communicator.impl.ELM327Communicator;
import it.unibo.javadyno.model.dyno.impl.AbstractPhysicalDyno;
import it.unibo.javadyno.model.dyno.obd2.api.Mode;
import it.unibo.javadyno.model.dyno.obd2.api.PID;

/**
 * Implementation of the Dyno interface.
 * It retrieve engine RPM, vehicle speed, and other vehicle data from the OBD2 line
 * and packets them in a RawData object when requested.
 */
public final class OBD2Dyno extends AbstractPhysicalDyno<String> {

    private static final int DEFAULT_OBD2_POLLING = 20;
    private static final int DATA_NUMERICAL_BASE = 16;
    private static final int HEADER_LENGTH = 4;
    private static final int SEGMENTS_LENGTH = 2;
    private static final int MODE_OFFSET = 40;
    private static final int RPM_MULTIPLIER = 256;
    private static final String THREAD_NAME = "OBD2Dyno";
    private static final String COMMAND_FORMAT = "%02X%02X";
    private final List<PID> supportedPIDs;
    private final LoopingIterator<PID> loopingIterator;
    private Optional<Integer> engineRpm = Optional.empty();
    private Optional<Integer> vehicleSpeed = Optional.empty();
    private Optional<Double> engineTemperature = Optional.empty();
    private Optional<Integer> barometricPressure = Optional.empty();
    private Optional<Integer> ambientAirTemperature = Optional.empty();
    private Optional<Instant> timestamp = Optional.empty();

    /**
     * Initializes the OBD2Dyno with default values.
     */
    public OBD2Dyno() {
        this(DEFAULT_OBD2_POLLING);
    }

    /**
     * Initializes the OBD2Dyno with a specified communicator and default polling interval.
     *
     * @param communicator the MCUCommunicator to use for communication
     */
    public OBD2Dyno(final MCUCommunicator<String> communicator) {
        this(communicator, DEFAULT_OBD2_POLLING);
    }

    /**
     * Initializes the OBD2Dyno with a specified polling interval and a default communicator.
     *
     * @param polling the polling interval in milliseconds
     */
    public OBD2Dyno(final int polling) {
        this(new ELM327Communicator(), polling);
    }

    /**
     * Initializes the OBD2Dyno with a specified communicator and polling interval.
     *
     * @param communicator the MCUCommunicator to use for communication
     * @param polling the polling interval in milliseconds
     */
    public OBD2Dyno(final MCUCommunicator<String> communicator, final int polling) {
        super(communicator, polling);
        this.supportedPIDs = List.of(
            PID.ENGINE_RPM,
            PID.VEHICLE_SPEED,
            PID.ENGINE_COOLANT_TEMPERATURE,
            PID.BAROMETRIC_PRESSURE,
            PID.AMBIENT_AIR_TEMPERATURE
        );
        this.loopingIterator = new LoopingIterator<>(this.supportedPIDs);
    }

    @Override
    public RawData getRawData() {
        return RawData.builder()
                .engineRPM(this.engineRpm)
                .vehicleSpeed(this.vehicleSpeed)
                .engineTemperature(this.engineTemperature)
                .baroPressure(this.barometricPressure) // Using boostPressure field for barometric pressure
                .ambientAirPressure(this.ambientAirTemperature) // Using ambientAirPressure field for ambient air temperature
                .timestamp(this.timestamp)
                .build();
    }

    @Override
    public DataSource getDynoType() {
        return DataSource.OBD2;
    }

    /**
     * Processes OBD2 response messages in format [Mode+40][PID][Data...].
     * The first 4 hex digits represent the echoed mode and PID from the request,
     * with the mode incremented by 0x40 to indicate a response.
     * Example: reuest "01 0C" gets response "41 0C 1A 2B".
     * Extracts and updates engine RPM, vehicle speed, and coolant temperature
     * based on the PID type, then sets the current timestamp.
     *
     * @param message the OBD2 response message in hexadecimal format
     */
    @Override
    protected void handleMessage(final String message) {
        this.timestamp = Optional.of(Instant.now());
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
                // Engine coolant temperature formula: temperature in °C = A - 40
                final String tempData = message.substring(HEADER_LENGTH, HEADER_LENGTH + SEGMENTS_LENGTH);
                this.engineTemperature = Optional.of((double)
                    (Integer.parseInt(tempData, DATA_NUMERICAL_BASE)
                    + PID.ENGINE_COOLANT_TEMPERATURE.getOffset())
                );
            } else if (pid == PID.BAROMETRIC_PRESSURE.getCode()) {
                final String pressureData = message.substring(HEADER_LENGTH, HEADER_LENGTH + SEGMENTS_LENGTH);
                this.barometricPressure = Optional.of(
                    Integer.parseInt(pressureData, DATA_NUMERICAL_BASE)
                    + PID.BAROMETRIC_PRESSURE.getOffset()
                );
            } else if (pid == PID.AMBIENT_AIR_TEMPERATURE.getCode()) {
                // Ambient air temperature formula: temperature in °C = A - 40
                final String ambientTempData = message.substring(HEADER_LENGTH, HEADER_LENGTH + SEGMENTS_LENGTH);
                this.ambientAirTemperature = Optional.of(
                    Integer.parseInt(ambientTempData, DATA_NUMERICAL_BASE)
                    + PID.AMBIENT_AIR_TEMPERATURE.getOffset()
                );
            }
        }
    }

    @Override
    protected String getOutgoingMessage() {
        return String.format(
            COMMAND_FORMAT,
            Mode.CURRENT_DATA.getCode(),
            this.loopingIterator.next().getCode());
    }

    @Override
    protected String getThreadName() {
        return THREAD_NAME;
    }

}

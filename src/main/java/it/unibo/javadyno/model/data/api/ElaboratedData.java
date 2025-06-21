package it.unibo.javadyno.model.data.api;

/**
 * ElaboratedData record that wraps a RawData instance
 * and provides additional calculated fields.
 *
 * @param rawData the RawData instance containing the raw data.
 * @param enginePowerKW the engine power in kilowatts.
 * @param enginePowerHP the engine power in horsepower.
 * @param engineCorrectedTorque the corrected torque of the engine.
 */
public record ElaboratedData(
    RawData rawData,
    Double enginePowerKW,
    Double enginePowerHP,
    Double engineCorrectedTorque
) {
}

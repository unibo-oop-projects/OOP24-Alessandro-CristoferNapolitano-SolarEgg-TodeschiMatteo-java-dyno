package it.unibo.javadyno.model.dyno.real.impl;

import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.RawData;
import it.unibo.javadyno.model.data.communicator.api.MCUCommunicator;
import it.unibo.javadyno.model.dyno.impl.AbstractPhysicalDyno;
import it.unibo.javadyno.model.dyno.real.api.RealDyno;

/**
 * Implementation of the RealDyno interface.
 * This class extends AbstractPhysicalDyno and provides methods to interact with a real dynamometer.
 */
public class ReadlDynoImpl extends AbstractPhysicalDyno implements RealDyno {

    /**
     * Initializes the ReadlDynoImpl with the given MCUCommunicator.
     *
     * @param communicator the MCUCommunicator to use for communication.
     */
    public ReadlDynoImpl(final MCUCommunicator communicator) {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRawData'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleMessage'");
    }

}

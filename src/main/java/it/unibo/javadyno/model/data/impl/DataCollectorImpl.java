package it.unibo.javadyno.model.data.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import it.unibo.javadyno.model.data.api.DataCollector;
import it.unibo.javadyno.model.data.api.DataElaborator;
import it.unibo.javadyno.model.data.api.ElaboratedData;

/**
 * Implementation of the DataCollector interface.
 * This class collects and stores elaborated data in a queue.
 */
public class DataCollectorImpl implements DataCollector {

    private final Queue<ElaboratedData> datas = new LinkedList<>();
    private final DataElaborator dataElaborator;

    /**
     * Constructor for DataCollectorImpl.
     * @param dataElaborator the reference to data elaborator to use for collecting data
     */
    public DataCollectorImpl(final DataElaborator dataElaborator) {
        this.dataElaborator = dataElaborator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearData() {
        this.datas.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collectData() {
        this.datas.add(this.dataElaborator.getElaboratedData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Queue<ElaboratedData> getFullData() {
        return (Queue<ElaboratedData>) Collections.unmodifiableCollection(datas);
    }
}

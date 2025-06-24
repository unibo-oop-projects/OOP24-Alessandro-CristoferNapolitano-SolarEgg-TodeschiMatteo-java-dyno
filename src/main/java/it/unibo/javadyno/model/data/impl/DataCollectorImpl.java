package it.unibo.javadyno.model.data.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import it.unibo.javadyno.model.data.api.DataCollector;
import it.unibo.javadyno.model.data.api.ElaboratedData;

/**
 * Implementation of the DataCollector interface.
 * This class collects and stores elaborated data in a queue.
 */
public class DataCollectorImpl implements DataCollector {

    private final Queue<ElaboratedData> datas = new LinkedList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void collectData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'collectData'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Queue<ElaboratedData> getFullData() {
        return (Queue<ElaboratedData>) Collections.unmodifiableCollection(datas);
    }
}

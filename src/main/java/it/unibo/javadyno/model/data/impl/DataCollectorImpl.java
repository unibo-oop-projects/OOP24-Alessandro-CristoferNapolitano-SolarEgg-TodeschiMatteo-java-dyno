package it.unibo.javadyno.model.data.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import it.unibo.javadyno.model.data.api.DataCollector;
import it.unibo.javadyno.model.data.api.DataElaborator;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Implementation of the DataCollector interface.
 * This class collects and stores elaborated data in a queue.
 */
public final class DataCollectorImpl implements DataCollector {

    private final Queue<ElaboratedData> datas;
    private DataElaborator dataElaborator;

    /**
     * Creates a new DataCollectorImpl instance.
     */
    public DataCollectorImpl() {
        this.datas = new LinkedList<>();
    }

    @Override
    public void initialize(final Dyno dynoSource) {
        this.datas.clear();
        this.dataElaborator = new DataElaboratorImpl(Objects.requireNonNull(dynoSource, "Dyno source cannot be null"));
    }

    @Override
    public void collectData() {
        final ElaboratedData elaboratedData = this.dataElaborator.getElaboratedData();
        if (!Objects.isNull(elaboratedData)) {
            this.datas.add(this.dataElaborator.getElaboratedData());
        }
    }

    @Override
    public Queue<ElaboratedData> getFullData() {
        return (Queue<ElaboratedData>) Collections.unmodifiableCollection(datas);
    }
}

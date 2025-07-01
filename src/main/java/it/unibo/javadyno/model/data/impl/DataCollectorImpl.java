package it.unibo.javadyno.model.data.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import it.unibo.javadyno.model.data.api.DataCollector;
import it.unibo.javadyno.model.data.api.DataElaborator;
import it.unibo.javadyno.model.data.api.ElaboratedData;
import it.unibo.javadyno.model.data.api.UserSettings;
import it.unibo.javadyno.model.dyno.api.Dyno;

/**
 * Implementation of the DataCollector interface.
 * This class collects and stores elaborated data in a list.
 */
public final class DataCollectorImpl implements DataCollector {

    private final List<ElaboratedData> datas;
    private DataElaborator dataElaborator;

    /**
     * Creates a new DataCollectorImpl instance.
     */
    public DataCollectorImpl() {
        this.datas = new ArrayList<>();
    }

    @Override
    public void initialize(final Dyno dynoSource, final UserSettings userSettings) {
        this.datas.clear();
        this.dataElaborator = new DataElaboratorImpl(
            Objects.requireNonNull(dynoSource, "Dyno source cannot be null"), 
            Objects.requireNonNull(userSettings, "User settings cannot be null"));
    }

    @Override
    public ElaboratedData collectData() {
        final ElaboratedData elaboratedData = this.dataElaborator.getElaboratedData();
        if (!Objects.isNull(elaboratedData)) {
            this.datas.add(elaboratedData);
        }
        return elaboratedData;
    }

    @Override
    public List<ElaboratedData> getFullData() {
        return List.copyOf(this.datas);
    }
}

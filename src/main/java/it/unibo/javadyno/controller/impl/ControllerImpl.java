package it.unibo.javadyno.controller.impl;

import java.util.Objects;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.model.data.api.DataCollector;
import it.unibo.javadyno.model.data.api.DataSource;
import it.unibo.javadyno.model.data.api.DataTransreciever;
import it.unibo.javadyno.model.data.impl.DataCollectorImpl;
import it.unibo.javadyno.model.dyno.api.Dyno;
import it.unibo.javadyno.model.dyno.simulated.impl.SimulatedDynoImpl;
import it.unibo.javadyno.view.impl.ViewImpl;
import javafx.application.Application;

/**
 * Controller implementation.
 */
public class ControllerImpl implements Controller {

    private Dyno dyno;
    private final DataCollector dataCollector;
    private final DataTransreciever dataTransreciever;

    /**
     * Default constructor for ControllerImpl.
     */
    public ControllerImpl() {
        this.dyno = null;
        this.dataCollector = new DataCollectorImpl();
        this.dataTransreciever = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void launchApp(final String[] args) {
        Application.launch(ViewImpl.class, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeApp() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSimulation() {
        if (!this.dyno.isActive()) {
            this.dyno = new SimulatedDynoImpl();
            this.dataCollector.clearData();
            this.dataTransreciever.begin(DataSource.SIMULATED_DYNO, this.dyno);
            this.dyno.begin();
        } else {
            throw new IllegalStateException("Simulation is already running."); // maybe alert box?
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopSimulation() {
        Objects.requireNonNull(this.dyno, "Dyno must be initialized before stopping it."); // maybe alert box?
        if (this.dyno.isActive()) {
            this.dyno.end();
            this.dyno = null;
        }
    }

}

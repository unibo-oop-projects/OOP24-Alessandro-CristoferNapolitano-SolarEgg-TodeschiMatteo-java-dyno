package it.unibo.javadyno.controller.impl;

import it.unibo.javadyno.controller.api.Controller;
import it.unibo.javadyno.view.impl.ViewImpl;
import javafx.application.Application;

/**
 * Controller implementation.
 */
public class ControllerImpl implements Controller {

    /**
     * @inheritDoc
     */
    @Override
    public void launchApp(final String[] args) {
        Application.launch(ViewImpl.class, args);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void closeApp() {
    }

}

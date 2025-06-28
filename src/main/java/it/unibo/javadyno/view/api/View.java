package it.unibo.javadyno.view.api;

import it.unibo.javadyno.model.data.api.ElaboratedData;
import javafx.stage.Stage;

/**
 * View interface.
 */
public interface View {

    /**
     * Start the view with the given primary stage.
     *
     * @param primaryStage the primary stage for this view
     */
    void begin(Stage primaryStage);

    /**
     * Update the view with the given data.
     *
     * @param data the data to update the view with
     */
    void update(ElaboratedData data);

}

package it.unibo.javadyno.model.graph.api;

import javafx.scene.chart.LineChart;

/**
 * Interface for managing charts in the JavaDyno application.
 *
 * @param <X> the type of the x-axis values
 * @param <Y> the type of the y-axis values
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface ChartsManager<X, Y> {
    /**
     * Adds a series to the chart with the specified name.
     *
     * @param chart the chart to which the series will be added
     * @param seriesName the name of the series to be added
     */
    void addNewSeries(LineChart<X, Y> chart, String seriesName);
}

package it.unibo.javadyno.model.graph.api;

import org.jfree.chart.JFreeChart;

/**
 * Interface for managing charts in the JavaDyno application.
 *
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface ChartsManager {
    /**
     * Adds a series to the chart with the specified name.
     *
     * @param chart the chart to which the series will be added
     * @param seriesName the name of the series to be added
     */
    void addNewSeries(JFreeChart chart, String seriesName);

    /**
     * Adds a point to the specified series in the chart.
     *
     * @param chart the chart containing the series
     * @param seriesName the name of the series to which the point will be added
     * @param xValue the x-axis value of the point
     * @param yValue the y-axis value of the point
     */
    void addPointToSeries(JFreeChart chart, String seriesName, Number xValue, Number yValue);
}

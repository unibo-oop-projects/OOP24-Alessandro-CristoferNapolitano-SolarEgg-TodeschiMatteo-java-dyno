package it.unibo.javadyno.model.graph.impl;

import it.unibo.javadyno.model.graph.api.ChartsManager;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 * Implementation of the ChartsManager interface for managing charts.
 *
 * @param <X> the type of the x-axis values
 * @param <Y> the type of the y-axis values
 */
public class ChartsManagerImpl<X, Y> implements ChartsManager<X, Y> {
    /**
     * {@inheritDoc}
     */
    @Override
    public void addNewSeries(final LineChart<X, Y> lineChart, final String seriesName) {
        final XYChart.Series<X, Y> series = new XYChart.Series<>();
        series.setName(seriesName);
        lineChart.getData().add(series);
    }
}

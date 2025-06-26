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

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPointToSeries(final LineChart<X, Y> chart, final String seriesName, final X xValue, final Y yValue) {
        chart.getData().stream()
            .filter(series -> series.getName().equals(seriesName))
            .findFirst()
            .ifPresent(series -> {
                final XYChart.Data<X, Y> point = new XYChart.Data<>(xValue, yValue);
                series.getData().add(point);
            });
    }
}

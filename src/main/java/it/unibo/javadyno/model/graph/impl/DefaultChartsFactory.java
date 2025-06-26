package it.unibo.javadyno.model.graph.impl;

import it.unibo.javadyno.model.graph.api.ChartsFactory;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

/**
 * Default implementation of the ChartsFactory functional interface.
 */
public class DefaultChartsFactory implements ChartsFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public LineChart<Number, Number> createEmptyTorqueCharts(
        final String title,
        final String xAxisLabel,
        final String yAxisLabel
        ) {
            final NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel(xAxisLabel);
            final NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel(yAxisLabel);
            final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle(title);
            lineChart.setCreateSymbols(false);
            return lineChart;
    }
}

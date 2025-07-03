package it.unibo.javadyno.view.visual.impl;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

import it.unibo.javadyno.view.visual.api.ChartsFactory;

/**
 * Default implementation of the ChartsFactory functional interface.
 */
public class DefaultChartsFactory implements ChartsFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public JFreeChart createEmptyCharts(
        final String title,
        final String xAxisLabel,
        final String yAxisLabel
        ) {
            final XYSeriesCollection collection = new XYSeriesCollection();
            return ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                collection,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
            );
    }
}

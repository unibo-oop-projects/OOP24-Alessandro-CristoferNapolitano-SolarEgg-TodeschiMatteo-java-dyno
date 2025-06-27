package it.unibo.javadyno.model.graph.impl;

import java.util.Objects;
import java.util.Optional;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import it.unibo.javadyno.controller.impl.AlertMonitor;
import it.unibo.javadyno.model.graph.api.ChartsManager;

/**
 * Implementation of the ChartsManager interface for managing charts.
 */
public class ChartsManagerImpl implements ChartsManager {
    /**
     * {@inheritDoc}
     */
    @Override
    public void addNewSeries(final JFreeChart lineChart, final String seriesName) {
        final XYSeries newSeries = new XYSeries(seriesName);
        final XYSeriesCollection dataset = (XYSeriesCollection) lineChart.getXYPlot().getDataset();
        dataset.addSeries(newSeries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPointToSeries(final JFreeChart chart, final String seriesName, final Number xValue, final Number yValue) {
        final XYSeriesCollection dataset = (XYSeriesCollection) chart.getXYPlot().getDataset();
        final XYSeries serie = dataset.getSeries(seriesName);
        if (!Objects.nonNull(serie)) {
            AlertMonitor.errorNotify(
                "Error in accessing the datas from charts",
                Optional.empty()
            );
            return;
        }
        serie.add(xValue, yValue);
    }
}

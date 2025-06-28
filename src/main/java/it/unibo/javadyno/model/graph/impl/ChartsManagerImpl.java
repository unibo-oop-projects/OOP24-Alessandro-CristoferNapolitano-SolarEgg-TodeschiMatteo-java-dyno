package it.unibo.javadyno.model.graph.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import java.awt.Color; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
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
    public void setColor(JFreeChart chart, String seriesName, YAxisLevel level, Color color) {
        final XYSeriesCollection dataset = (XYSeriesCollection) chart.getXYPlot().getDataset(level.getLevel());
        if (!Objects.nonNull(dataset)) {
            AlertMonitor.errorNotify(
                "Error in dataset from charts",
                Optional.of("Series '" + seriesName + "' does not exist in level " + level.getLevel())
            );
            return;
        }
        final XYSeries serie = dataset.getSeries(seriesName);
        if (!Objects.nonNull(serie)) {
            AlertMonitor.errorNotify(
                "Error in accessing series from charts",
                Optional.of("Series '" + seriesName + "' does not exist in level " + level.getLevel())
            );
            return;
        }
        final XYItemRenderer renderer = chart.getXYPlot().getRenderer(level.getLevel());
        if (!Objects.nonNull(renderer)) {
            AlertMonitor.errorNotify(
                "Error in accessing the renderer from charts",
                Optional.of("Level " + level.getLevel() + " does not exist.")
            );
            return;
        }
        final int seriesIndex = IntStream.range(0, dataset.getSeriesCount())
            .filter(i -> dataset.getSeries(i).getKey().equals(seriesName))
            .findFirst()
            .orElse(0);
        renderer.setSeriesPaint(seriesIndex, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNewSeries(final JFreeChart lineChart, final String seriesName, final ChartsManager.YAxisLevel level) {
        final XYSeries newSeries = new XYSeries(seriesName);
        final XYSeriesCollection dataset = (XYSeriesCollection) lineChart.getXYPlot().getDataset(level.getLevel());
        if (!Objects.nonNull(dataset)) {
            AlertMonitor.errorNotify(
                "Error in accessing the dataset from charts",
                Optional.of("Level " + level.getLevel() + " does not exist.")
            );
            return;
        }
        dataset.addSeries(newSeries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPointToSeries(
        final JFreeChart chart, final String seriesName, final ChartsManager.YAxisLevel level,
        final Number xValue, final Number yValue
        ) {
        final XYSeriesCollection dataset = (XYSeriesCollection) chart.getXYPlot().getDataset(level.getLevel());
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void addYAxis(final JFreeChart chart, final String axisLabel) {
        final XYPlot plot = chart.getXYPlot();
        if (plot.getRangeAxisCount() >= ChartsManager.YAxisLevel.MAX.getLevel()) {
            AlertMonitor.errorNotify(
                "Error in adding a new Y-axis",
                Optional.of("The maximum number of Y-axes is" + ChartsManager.YAxisLevel.MAX.getLevel())
            );
            return;
        }
        final int newIndex = plot.getRangeAxisCount();
        final NumberAxis newYAxis = new NumberAxis(axisLabel);
        plot.setRangeAxis(newIndex, newYAxis);
        final XYSeriesCollection newDataset = new XYSeriesCollection();
        plot.setDataset(newIndex, newDataset);
        plot.mapDatasetToRangeAxis(newIndex, newIndex);
        final XYLineAndShapeRenderer newRenderer = new XYLineAndShapeRenderer();
        newRenderer.setDefaultLinesVisible(true);
        newRenderer.setDefaultShapesVisible(false);
        plot.setRenderer(newIndex, newRenderer);
    }
}

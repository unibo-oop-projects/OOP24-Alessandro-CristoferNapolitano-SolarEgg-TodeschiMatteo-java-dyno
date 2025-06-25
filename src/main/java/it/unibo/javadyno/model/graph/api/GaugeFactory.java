package it.unibo.javadyno.model.graph.api;

import eu.hansolo.medusa.Gauge;

/**
 * Factory interface for creating charts.
 * This interface uses a factory method to create a Gauge chart.
 */
@FunctionalInterface
public interface GaugeFactory {
    /**
     * The factory method for creating a chart.
     *
     * @param title the title of the chart.
     * @param unit the unit of measurement for the chart.
     * @param minValue the minimum value of the chart.
     * @param maxValue the maximum value of the chart.
     * @param majorTickSpace the space between major ticks.
     * @param minorTickSpace the space between minor ticks.
     *
     * @return a configured Gauge chart.
     */
    Gauge createGaugeChart(
        String title,
        String unit,
        int minValue,
        int maxValue,
        int majorTickSpace,
        int minorTickSpace
    );
}

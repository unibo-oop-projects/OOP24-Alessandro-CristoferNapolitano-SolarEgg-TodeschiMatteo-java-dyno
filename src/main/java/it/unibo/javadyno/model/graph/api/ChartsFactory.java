package it.unibo.javadyno.model.graph.api;

import org.jfree.chart.JFreeChart;

/**
 * Factory interface for creating charts.
 */
@FunctionalInterface
public interface ChartsFactory {
    /**
     * Creates an empty torque chart with the specified parameters.
     *
     * @param title the title of the chart
     * @param xAxisLabel the label for the x-axis
     * @param yAxisLabel the label for the y-axis
     *
     * @return an empty LineChart with the specified parameters
     */
    JFreeChart createEmptyCharts(
        String title,
        String xAxisLabel,
        String yAxisLabel
    );
}

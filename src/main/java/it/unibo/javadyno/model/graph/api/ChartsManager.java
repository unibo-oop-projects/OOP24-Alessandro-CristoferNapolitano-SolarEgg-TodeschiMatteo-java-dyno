package it.unibo.javadyno.model.graph.api;

import org.jfree.chart.JFreeChart;

/**
 * Interface for managing charts in the JavaDyno application.
 *
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface ChartsManager {

    public static enum YAxisLevel {
        FIRST(0),
        SECOND(1),
        THIRD(2),
        MAX(3);

        private final int level;

        YAxisLevel(final int level) {
            this.level = level;
        }

        public int getLevel() {
            return this.level;
        }

    }
    /**
     * Adds a series to the chart with the specified name.
     *
     * @param chart the chart to which the series will be added
     * @param seriesName the name of the series to be added
     * @param level the Y-axis level for the series
     */
    void addNewSeries(JFreeChart chart, String seriesName, final ChartsManager.YAxisLevel level);

    /**
     * Adds a point to the specified series in the chart.
     *
     * @param chart the chart containing the series
     * @param seriesName the name of the series to which the point will be added
     * @param xValue the x-axis value of the point
     * @param yValue the y-axis value of the point
     */
    void addPointToSeries(JFreeChart chart, String seriesName, final ChartsManager.YAxisLevel level,Number xValue, Number yValue);

    /**
     * Adds another a Y-axis to the chart with the specified label and series name.
     *
     * @param chart the chart to which the Y-axis will be added
     * @param axisLabel the label for the Y-axis
     */
    void addYAxis(JFreeChart chart, String axisLabel);
}

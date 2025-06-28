package it.unibo.javadyno.model.graph.api;

import java.awt.Color; 
import org.jfree.chart.JFreeChart;

/**
 * Interface for managing charts in the JavaDyno application.
 *
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface ChartsManager {

    /**
     * Enumeration representing the levels of Y-axis in a chart.
     */
    enum YAxisLevel {
        FIRST(0),
        SECOND(1),
        THIRD(2),
        MAX(3);

        private final int level;

        YAxisLevel(final int level) {
            this.level = level;
        }

        /**
         * Returns the integer value representing the Y-axis level.
         *
         * @return the level as an integer
         */
        public int getLevel() {
            return this.level;
        }

    }

    /**
     * Sets the color of a series in the chart for a specific Y-axis level.
     *
     * @param chart the chart containing the series
     * @param seriesName the name of the series whose color will be set
     * @param level the Y-axis level of the series
     * @param color the color to set for the series
     */
    void setColor(JFreeChart chart, String seriesName, ChartsManager.YAxisLevel level, Color color);

    /**
     * Adds a series to the chart with the specified name.
     *
     * @param chart the chart to which the series will be added
     * @param seriesName the name of the series to be added
     * @param level the Y-axis level for the series
     */
    void addNewSeries(JFreeChart chart, String seriesName, ChartsManager.YAxisLevel level);

    /**
     * Adds a point to the specified series in the chart.
     *
     * @param chart the chart containing the series
     * @param seriesName the name of the series to which the point will be added
     * @param level the Y-axis level of the series
     * @param xValue the x-axis value of the point
     * @param yValue the y-axis value of the point
     */
    void addPointToSeries(JFreeChart chart, String seriesName, ChartsManager.YAxisLevel level, Number xValue, Number yValue);

    /**
     * Adds another a Y-axis to the chart with the specified label and series name.
     *
     * @param chart the chart to which the Y-axis will be added
     * @param axisLabel the label for the Y-axis
     */
    void addYAxis(JFreeChart chart, String axisLabel);
}

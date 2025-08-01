package it.unibo.javadyno.view.visual.api;

import java.awt.Color;
import java.util.List;

import org.jfree.chart.JFreeChart;

/**
 * Interface for managing charts in the JavaDyno application.
 *
 */
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
     * Sets the chart to use a dark theme.
     *
     * @param chart the chart to be themed
     */
    void setDarkTheme(JFreeChart chart);

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
     * Sets a background image for the chart.
     *
     * @param chart the JFreeChart to set the background image for
     * @param imagePath the path to the image file
     */
    void setBackgroundImage(JFreeChart chart, String imagePath);

    /**
     * Removes the background image from the chart.
     *
     * @param chart the JFreeChart from which the background image will be removed
     */
    void resetBackgroundImage(JFreeChart chart);

    /**
     * Adds a series to the chart with the specified name.
     *
     * @param chart the chart to which the series will be added
     * @param seriesName the name of the series to be added
     * @param level the Y-axis level for the series
     */
    void addNewSeries(JFreeChart chart, String seriesName, ChartsManager.YAxisLevel level);

    /**
     * Sets the visibility of a series in the chart.
     *
     * @param chart the chart containing the series
     * @param seriesIndex the index of the series to be set visible or invisible
     * @param isVisible true to make the series visible, false to hide it
     */
    void setSeriesVisibility(JFreeChart chart, int seriesIndex, boolean isVisible);

    /**
     * Adds a point to the specified series in the chart.
     *
     * @param chart the chart containing the series
     * @param seriesName the name of the series to which the point will be added
     * @param level the Y-axis level of the series
     * @param xValue the x-axis value of the point
     * @param yValue the y-axis value of the point
     */
    void addPointToSeries(
        JFreeChart chart, String seriesName, ChartsManager.YAxisLevel level,
        Number xValue, Number yValue
    );

    /**
     * Adds all points from the provided iterables to the specified series in the chart.
     *
     * @param chart the chart containing the series
     * @param seriesName the name of the series to which the points will be added
     * @param level the Y-axis level of the series
     * @param xValues the list of x-axis values to be added
     * @param yValues the list of y-axis values to be added
     */
    void addAllPointsToSeries(
        JFreeChart chart, String seriesName, ChartsManager.YAxisLevel level,
        List<Number> xValues, List<Number> yValues
    );

    /**
     * Adds another a Y-axis to the chart with the specified label and series name.
     *
     * @param chart the chart to which the Y-axis will be added
     * @param axisLabel the label for the Y-axis
     */
    void addYAxis(JFreeChart chart, String axisLabel);
}

package it.unibo.javadyno.view.impl.component;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;

import it.unibo.javadyno.model.graph.api.ChartsFactory;
import it.unibo.javadyno.model.graph.api.ChartsManager;
import it.unibo.javadyno.model.graph.impl.ChartsManagerImpl;
import it.unibo.javadyno.model.graph.impl.DefaultChartsFactory;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * ChartsPanel class for displaying charts in the JavaDyno application.
 */
public final class ChartsPanel extends VBox {
    private static final String CSS_CHARTS_PANEL_TAG = "charts-panel";
    private static final double CHART_HEIGH_FACTOR = 0.7;
    private static final double CHART_WIDTH_FACTOR = 0.6;
    private static final double CHART_MINIMUM_FACTOR = 0.5;
    private static final String CHARTS_NAME = "Speed";
    private static final String X_AXIS_LABEL = "Time (h)";
    private static final String Y_AXIS_LABEL = "Position (Km)";
    private static final String SERIES_NAME = "Speed (Km/h)";

    private final JFreeChart lineChart;
    private final ChartsFactory chartsFactory = new DefaultChartsFactory();
    private final ChartsManager chartManager = new ChartsManagerImpl();

    /**
     * Default constructor for ChartsPanel.
     */
    public ChartsPanel() {
        this.setAlignment(Pos.TOP_RIGHT);
        this.getStyleClass().add(CSS_CHARTS_PANEL_TAG);
        final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        lineChart = chartsFactory.createEmptyCharts(
            CHARTS_NAME,
            X_AXIS_LABEL,
            Y_AXIS_LABEL
        );
        final ChartViewer viewer = new ChartViewer(lineChart);
        viewer.setPrefSize(screenBounds.getWidth() * CHART_WIDTH_FACTOR, screenBounds.getHeight() * CHART_HEIGH_FACTOR);
        viewer.setMinSize(screenBounds.getWidth() * CHART_MINIMUM_FACTOR, screenBounds.getHeight() * CHART_MINIMUM_FACTOR);
        chartManager.addNewSeries(lineChart, SERIES_NAME, ChartsManager.YAxisLevel.FIRST);
        chartManager.addYAxis(lineChart, Y_AXIS_LABEL);
        chartManager.addNewSeries(lineChart, SERIES_NAME, ChartsManager.YAxisLevel.SECOND);
        chartManager.addYAxis(lineChart, Y_AXIS_LABEL);
        chartManager.addNewSeries(lineChart, SERIES_NAME, ChartsManager.YAxisLevel.THIRD);
        this.getChildren().add(viewer);
    }

    /**
     * Adds a point to the speed chart.
     *
     * @param xValue the x-axis value (time in hours)
     * @param yValue the y-axis value (position in Km)
     */
    public void addPointToChart(final Number xValue, final Number yValue) {
        chartManager.addPointToSeries(
            this.lineChart,
            SERIES_NAME,
            ChartsManager.YAxisLevel.FIRST,
            xValue,
            yValue
        );
        chartManager.addPointToSeries(
            this.lineChart,
            SERIES_NAME,
            ChartsManager.YAxisLevel.SECOND,
            yValue,
            xValue
        );
        chartManager.addPointToSeries(
            this.lineChart,
            SERIES_NAME,
            ChartsManager.YAxisLevel.THIRD,
            yValue,
            yValue
        );
    }
}

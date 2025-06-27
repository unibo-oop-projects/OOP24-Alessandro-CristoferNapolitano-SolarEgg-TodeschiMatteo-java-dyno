package it.unibo.javadyno.view.impl.component;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;

import it.unibo.javadyno.model.graph.api.ChartsFactory;
import it.unibo.javadyno.model.graph.api.ChartsManager;
import it.unibo.javadyno.model.graph.impl.ChartsManagerImpl;
import it.unibo.javadyno.model.graph.impl.DefaultChartsFactory;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * ChartsPanel class for displaying charts in the JavaDyno application.
 */
public final class ChartsPanel extends VBox {
    private static final String CSS_CHARTS_PANEL_TAG = "charts-panel";
    private static final int COLUMN_SPACING = 5;
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
        super(COLUMN_SPACING);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add(CSS_CHARTS_PANEL_TAG);
        lineChart = chartsFactory.createEmptyCharts(
            CHARTS_NAME,
            X_AXIS_LABEL,
            Y_AXIS_LABEL
        );
        final ChartViewer viewer = new ChartViewer(lineChart);
        viewer.setPrefSize(600, 400);
        viewer.setMinSize(400, 300);
        chartManager.addNewSeries(lineChart, SERIES_NAME);
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
            xValue,
            yValue
        );
    }
}

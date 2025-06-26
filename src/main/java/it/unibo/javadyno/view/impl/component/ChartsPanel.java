package it.unibo.javadyno.view.impl.component;

import it.unibo.javadyno.model.graph.api.ChartsFactory;
import it.unibo.javadyno.model.graph.api.ChartsManager;
import it.unibo.javadyno.model.graph.impl.ChartsManagerImpl;
import it.unibo.javadyno.model.graph.impl.DefaultChartsFactory;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
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

    private LineChart<Number, Number> lineChart;
    private final ChartsFactory chartsFactory = new DefaultChartsFactory();
    private final ChartsManager<Number, Number> chartManager = new ChartsManagerImpl<>();

    /**
     * Default constructor for ChartsPanel.
     */
    public ChartsPanel() {
        super(COLUMN_SPACING);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add(CSS_CHARTS_PANEL_TAG);
        lineChart = chartsFactory.createEmptyTorqueCharts(
            CHARTS_NAME,
            X_AXIS_LABEL,
            Y_AXIS_LABEL
        );
        chartManager.addNewSeries(lineChart, SERIES_NAME);
        this.getChildren().add(lineChart);
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

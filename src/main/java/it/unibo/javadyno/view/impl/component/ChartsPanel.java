package it.unibo.javadyno.view.impl.component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;

import it.unibo.javadyno.model.graph.api.ChartsFactory;
import it.unibo.javadyno.model.graph.api.ChartsManager;
import it.unibo.javadyno.model.graph.impl.ChartsManagerImpl;
import it.unibo.javadyno.model.graph.impl.DefaultChartsFactory;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Screen;

/**
 * ChartsPanel class for displaying charts in the JavaDyno application.
 */
public final class ChartsPanel extends VBox {
    private static final String CSS_CHARTS_PANEL_TAG = "charts-panel";
    private static final String CSS_POPUP_TAG = "popup-panel";
    private static final String BG_IMAGE = "/images/logo_no_bg.png";
    private static final int POPUP_SPACING = 5;
    private static final double CHART_HEIGH_FACTOR = 0.7;
    private static final double CHART_WIDTH_FACTOR = 0.6;
    private static final double CHART_MINIMUM_FACTOR = 0.5;
    private static final String MANAGE_BUTTON = "Manage Series";
    private static final String CLOSE_BUTTON = "Close";
    private static final String CHARTS_NAME = "RPM vs Power";
    private static final String X_AXIS_LABEL = "RPM (Revolutions Per Minute)";
    private static final String Y_AXIS_LABEL = "Horsepower (HP)";
    private static final String Y2_AXIS_LABEL = "Torque (Nm)";
    private static final String FIRST_SERIES_NAME = "Power";
    private static final String SECOND_SERIES_NAME = "Torque";

    private final JFreeChart lineChart;
    private final ChartViewer viewer;
    private final Map<Button, Boolean> deleteButtons = new LinkedHashMap<>();
    private final Button manageButtons = new Button(MANAGE_BUTTON);
    private final Popup deletePopup = new Popup();
    private final ChartsFactory chartsFactory = new DefaultChartsFactory();
    private final ChartsManager chartManager = new ChartsManagerImpl();
    private int importedOrder = 1;

    /**
     * Default constructor for ChartsPanel.
     */
    public ChartsPanel() {
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add(CSS_CHARTS_PANEL_TAG);
        final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        this.lineChart = chartsFactory.createEmptyCharts(
            CHARTS_NAME,
            X_AXIS_LABEL,
            Y_AXIS_LABEL
        );
        manageButtons.setOnAction(e -> {
            if (deleteButtons.isEmpty()) {
                return;
            }
            final VBox popupContent = new VBox(POPUP_SPACING);
            final Button closeButton = new Button(CLOSE_BUTTON);
            closeButton.setOnAction(event -> deletePopup.hide());
            popupContent.getStyleClass().add(CSS_POPUP_TAG);
            popupContent.getChildren().setAll(deleteButtons.keySet());
            popupContent.getChildren().add(closeButton);
            deletePopup.getContent().setAll(popupContent);
            if (!deletePopup.isShowing()) {
                deletePopup.show(this.getScene().getWindow());
            } else {
                deletePopup.hide();
            }
        });
        viewer = new ChartViewer(this.lineChart);
        viewer.setPrefSize(screenBounds.getWidth() * CHART_WIDTH_FACTOR, screenBounds.getHeight() * CHART_HEIGH_FACTOR);
        viewer.setMinSize(screenBounds.getWidth() * CHART_MINIMUM_FACTOR, screenBounds.getHeight() * CHART_MINIMUM_FACTOR);
        chartManager.addNewSeries(this.lineChart, FIRST_SERIES_NAME, ChartsManager.YAxisLevel.FIRST);
        chartManager.addYAxis(this.lineChart, Y2_AXIS_LABEL);
        chartManager.addNewSeries(this.lineChart, SECOND_SERIES_NAME, ChartsManager.YAxisLevel.SECOND);
        chartManager.setDarkTheme(this.lineChart);
        chartManager.setBackgroundImage(this.lineChart, BG_IMAGE);
        this.getChildren().addAll(viewer, manageButtons);
    }

    /**
     * Sets the vertical growth priority for the chart viewer.
     *
     * @param priority the priority for vertical growth
     */
    public void setChartViewerVgrow(final Priority priority) {
        setVgrow(this.viewer, priority);
    }

    /**
     * Adds a single data point to the charts panel.
     *
     * @param xValue the x-axis value
     * @param yValue the y-axis value
     * @param y2Valu the second y-axis value
     */
    public void addSingleData(final Number xValue, final Number yValue, final Number y2Valu) {
        addPointToChart(FIRST_SERIES_NAME, SECOND_SERIES_NAME, xValue, yValue, y2Valu);
    }

    /**
     * Inserts all data of a list into the charts panel.
     *
     * @param xValues the list of x-axis values
     * @param yValues the list of y-axis values
     * @param y2Values the list of second y-axis values
     */
    public void addAllData(final List<Number> xValues, final List<Number> yValues, final List<Number> y2Values) {
        final String firstSeriesName = String.format("%s(%d)", FIRST_SERIES_NAME, this.importedOrder);
        final String secondSeriesName = String.format("%s(%d)", SECOND_SERIES_NAME, this.importedOrder);
        final int order = this.importedOrder;
        final Button deleteButton = new Button("Hide Series " + order);
        deleteButtons.put(deleteButton, true);
        deleteButton.setOnAction(e -> {
            chartManager.setSeriesVisibility(this.lineChart, order, !this.deleteButtons.get(deleteButton));
            this.deleteButtons.put(deleteButton, !this.deleteButtons.get(deleteButton));
            if (this.deleteButtons.get(deleteButton)) {
                deleteButton.setText("Hide Series " + order);
            } else {
                deleteButton.setText("Show Series " + order);
            }
        });
        this.importedOrder++;
        chartManager.addNewSeries(this.lineChart, firstSeriesName, ChartsManager.YAxisLevel.FIRST);
        chartManager.addNewSeries(this.lineChart, secondSeriesName, ChartsManager.YAxisLevel.SECOND);
        chartManager.addAllPointsToSeries(this.lineChart, firstSeriesName, ChartsManager.YAxisLevel.FIRST, xValues, yValues);
        chartManager.addAllPointsToSeries(this.lineChart, secondSeriesName, ChartsManager.YAxisLevel.SECOND, xValues, y2Values);
    }

    /**
     * Adds a point to the speed chart.
     *
     * @param seriesName the name of the series to which the point will be added
     * @param xValue the x-axis value
     * @param yValue the y-axis value
     * @param y2Value the second y-axis value
     */
    private void addPointToChart(final String firstSeriesName, final String secondSeriesName, final Number xValue, final Number yValue, final Number y2Value) {
        chartManager.addPointToSeries(
            this.lineChart,
            firstSeriesName,
            ChartsManager.YAxisLevel.FIRST,
            xValue,
            yValue
        );
        chartManager.addPointToSeries(
            this.lineChart,
            secondSeriesName,
            ChartsManager.YAxisLevel.SECOND,
            xValue,
            y2Value
        );
    }

    /**
     * Sets the visibility of the background image.
     *
     * @param visible true to show the background image, false to hide it
     */
    public void setBackgroundVisible(final boolean visible) {
        if (visible) {
            chartManager.setBackgroundImage(this.lineChart, BG_IMAGE);
        } else {
            chartManager.resetBackgroundImage(this.lineChart);
        }
    }

    /**
     * Removes the first series from all Y-axis levels in the chart.
     */
    public void hideDefaultVisibility() {
        chartManager.setSeriesVisibility(this.lineChart, 0, false);
    }
}

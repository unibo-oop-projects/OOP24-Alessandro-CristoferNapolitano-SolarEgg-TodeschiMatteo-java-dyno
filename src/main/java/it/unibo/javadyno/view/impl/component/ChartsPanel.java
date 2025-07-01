package it.unibo.javadyno.view.impl.component;

import java.util.ArrayList;
import java.util.List;

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

    private static final String CHARTS_NAME = "RPM vs Power";
    private static final String X_AXIS_LABEL = "RPM (Revolutions Per Minute)";
    private static final String Y_AXIS_LABEL = "Horsepower (HP)";
    private static final String Y2_AXIS_LABEL = "Torque (Nm)";
    private static final String GENERAL_SERIES_NAME = "Power";

    private final JFreeChart lineChart;
    private final ChartViewer viewer;
    private final List<Button> deleteButtons = new ArrayList<>();
    private final Button manageButtons = new Button("Manage Series");
    private final Popup deletePopup = new Popup();
    private final ChartsFactory chartsFactory = new DefaultChartsFactory();
    private final ChartsManager chartManager = new ChartsManagerImpl();
    private int importedOrder=1;

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
            VBox popupContent = new VBox(POPUP_SPACING);
            popupContent.getStyleClass().add(CSS_POPUP_TAG);
            popupContent.getChildren().setAll(deleteButtons);
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
        chartManager.addNewSeries(this.lineChart, GENERAL_SERIES_NAME, ChartsManager.YAxisLevel.FIRST);
        chartManager.addYAxis(this.lineChart, Y2_AXIS_LABEL);
        chartManager.addNewSeries(this.lineChart, GENERAL_SERIES_NAME, ChartsManager.YAxisLevel.SECOND);
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
        addPointToChart(GENERAL_SERIES_NAME, xValue, yValue, y2Valu);
    }

    /**
     * Inserts all data of a list into the charts panel.
     *
     * @param xValues the list of x-axis values
     * @param yValues the list of y-axis values
     * @param y2Values the list of second y-axis values
     */
    public void addAllData(final List<Number> xValues, final List<Number> yValues, final List<Number> y2Values) {
        final String seriesName = String.format("%s(Import %d)", GENERAL_SERIES_NAME, this.importedOrder);
        final Button deleteButton = new Button("Delete " + seriesName);
        final int order = this.importedOrder;
        deleteButton.setOnAction(e -> {
            chartManager.disableSeries(this.lineChart, order);
            deleteButtons.remove(deleteButton);
            if (deletePopup.isShowing()) {
                ((VBox) deletePopup.getContent().get(0)).getChildren().remove(deleteButton);
                if (deleteButtons.isEmpty()) {
                    deletePopup.hide();
                }
            }
        });
        deleteButtons.add(deleteButton);
        this.importedOrder++;
        chartManager.addNewSeries(this.lineChart, seriesName, ChartsManager.YAxisLevel.FIRST);
        chartManager.addNewSeries(this.lineChart, seriesName, ChartsManager.YAxisLevel.SECOND);
        chartManager.addAllPointsToSeries(this.lineChart, seriesName, ChartsManager.YAxisLevel.FIRST, xValues, yValues);
        chartManager.addAllPointsToSeries(this.lineChart, seriesName, ChartsManager.YAxisLevel.SECOND, xValues, y2Values);
    }

    /**
     * Adds a point to the speed chart.
     *
     * @param seriesName the name of the series to which the point will be added
     * @param xValue the x-axis value
     * @param yValue the y-axis value
     * @param y2Value the second y-axis value
     */
    private void addPointToChart(final String seriesName, final Number xValue, final Number yValue, final Number y2Value) {
        chartManager.addPointToSeries(
            this.lineChart,
            seriesName,
            ChartsManager.YAxisLevel.FIRST,
            xValue,
            yValue
        );
        chartManager.addPointToSeries(
            this.lineChart,
            seriesName,
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
    public void removeDefaultSeries() {
        chartManager.disableSeries(this.lineChart, 0);
    }
}

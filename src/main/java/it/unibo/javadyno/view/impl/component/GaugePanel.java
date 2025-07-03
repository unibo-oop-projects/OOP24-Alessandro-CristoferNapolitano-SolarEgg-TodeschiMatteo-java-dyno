package it.unibo.javadyno.view.impl.component;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Marker;
import eu.hansolo.medusa.Section;
import it.unibo.javadyno.view.visual.api.GaugeFactory;
import it.unibo.javadyno.view.visual.impl.DefaultGaugeFactory;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

/**
 * GaugePanel class that extends VBox to create a panel with multiple gauges.
 */
public final class GaugePanel extends HBox {
    private static final double MAXIMUM_HEIGHT_FACTOR = 0.3;
    private static final int COLUMN_SPACING = 5;
    private static final String CSS_GAUGE_PANEL_TAG = "gauge-panel";

    private static final String RPM_CHARTS_TITLE = "Tachometer";
    private static final String RPM_CHARTS_UNIT = "RPM";
    private static final int RPM_MAX_RANGE = 8000;
    private static final int RPM_MAJOR_TICK_SPACE = 1000;
    private static final int RPM_MINOR_TICK_SPACE = 200;
    private static final int RPM_WARNING = 5000;
    private static final int RPM_REDLINE = 6000;

    private static final String SPEEDOMETER_TITLE = "Speedometer";
    private static final String SPEEDOMETER_UNIT = "KM/H";
    private static final int SPEEDOMETER_MAX_RANGE = 300;
    private static final int SPEEDOMETER_MAJOR_TICK_SPACE = 20;
    private static final int SPEEDOMETER_MINOR_TICK_SPACE = 5;

    private static final String TEMPERATURE_TITLE = "Temperature";
    private static final String TEMPERATURE_UNIT = "°C";
    private static final int TEMPERATURE_MAX_RANGE = 120;
    private static final int TEMPERATURE_MAJOR_TICK_SPACE = 20;
    private static final int TEMPERATURE_MINOR_TICK_SPACE = 5;

    private final Gauge rpmGauge;
    private final Gauge speedGauge;
    private final Gauge tempGauge;
    private final GaugeFactory gaugeFactory = new DefaultGaugeFactory();

    /**
     * Constructor for GaugePanel.
     */
    public GaugePanel() {
        super(COLUMN_SPACING);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add(CSS_GAUGE_PANEL_TAG);
        final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        final double height = screenBounds.getHeight() * MAXIMUM_HEIGHT_FACTOR;
        this.setMaxHeight(height);
        this.rpmGauge = gaugeFactory.createGaugeChart(
                RPM_CHARTS_TITLE,
                RPM_CHARTS_UNIT,
                0,
                RPM_MAX_RANGE,
                RPM_MAJOR_TICK_SPACE,
                RPM_MINOR_TICK_SPACE
        );
        this.rpmGauge.setMarkers(new Marker(RPM_WARNING, Color.RED));
        this.rpmGauge.setSections(new Section(RPM_REDLINE, RPM_MAX_RANGE, Color.RED));
        this.speedGauge = gaugeFactory.createGaugeChart(
                SPEEDOMETER_TITLE,
                SPEEDOMETER_UNIT,
                0,
                SPEEDOMETER_MAX_RANGE,
                SPEEDOMETER_MAJOR_TICK_SPACE,
                SPEEDOMETER_MINOR_TICK_SPACE
        );
        this.tempGauge = gaugeFactory.createGaugeChart(
                TEMPERATURE_TITLE,
                TEMPERATURE_UNIT,
                0,
                TEMPERATURE_MAX_RANGE,
                TEMPERATURE_MAJOR_TICK_SPACE,
                TEMPERATURE_MINOR_TICK_SPACE
        );
        this.getChildren().addAll(rpmGauge, speedGauge, tempGauge);
    }

    /**
     * Updates the gauges with new values.
     *
     * @param rpm the current RPM value
     * @param speed the current speed value
     * @param temperature the current temperature value
     */
    public void updateGauges(final Number rpm, final Number speed, final Number temperature) {
        this.rpmGauge.setValue(rpm.intValue());
        this.speedGauge.setValue(speed.doubleValue());
        this.tempGauge.setValue(temperature.doubleValue());
    }
}

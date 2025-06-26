package it.unibo.javadyno.model.graph.api;

import it.unibo.javadyno.model.graph.impl.DefaultGaugeFactory;
import eu.hansolo.medusa.Gauge;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * GaugePanel class that extends VBox to create a panel with multiple gauges.
 */
public final class DefaultGaugePanel extends VBox {
    private static final int COLUMN_SPACING = 5;

    // Constants for the gauges
    private static final String RPM_CHARTS_TITLE = "Tachometer";
    private static final String RPM_CHARTS_UNIT = "RPM";
    private static final int RPM_MAX_RANGE = 8000;
    private static final int RPM_MAJOR_TICK_SPACE = 1000;
    private static final int RPM_MINOR_TICK_SPACE = 200;

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
    public DefaultGaugePanel() {
        super(COLUMN_SPACING);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("gauge-panel");
        this.rpmGauge = gaugeFactory.createGaugeChart(
                RPM_CHARTS_TITLE,
                RPM_CHARTS_UNIT,
                0,
                RPM_MAX_RANGE,
                RPM_MAJOR_TICK_SPACE,
                RPM_MINOR_TICK_SPACE
        );
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

}

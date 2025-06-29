package it.unibo.javadyno.model.graph.impl;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import it.unibo.javadyno.model.graph.api.GaugeFactory;
import javafx.scene.paint.Color;

/**
 * Default implementation of the GaugeFactory interface.
 * This class provides a method to create a Gauge chart with specified parameters.
 */
public class DefaultGaugeFactory implements GaugeFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public Gauge createGaugeChart(
        final String title,
        final String unit,
        final int minValue,
        final int maxValue,
        final int majorTickSpace,
        final int minorTickSpace
    ) {
        return GaugeBuilder.create()
                .title(title)
                .unit(unit)
                .minValue(minValue)
                .maxValue(maxValue)
                .majorTickSpace(majorTickSpace)
                .minorTickSpace(minorTickSpace)
                .animated(true)// Handle animation?
                .decimals(0)
                .sectionsVisible(true)
                .markersVisible(true)
                .backgroundPaint(Color.WHITE)
                .borderPaint(Color.BLACK)
                .valueColor(Color.BLACK)
                .titleColor(Color.BLACK)
                .unitColor(Color.BLACK)
                .majorTickMarkColor(Color.BLACK)
                .minorTickMarkColor(Color.BLACK)
                .needleColor(Color.RED)
                .build();
    }
}

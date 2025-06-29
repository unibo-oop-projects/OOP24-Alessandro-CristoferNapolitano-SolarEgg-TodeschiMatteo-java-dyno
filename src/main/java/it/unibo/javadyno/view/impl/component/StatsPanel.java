package it.unibo.javadyno.view.impl.component;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * StatsPanel class that creates a panel for displaying global statistics.
 */
public final class StatsPanel extends VBox {
    private final Label maxRpmLabel;
    private final Label maxTorqueLabel;
    private final Label maxHorsePowerLabel;
    private final Label maxKiloWattsLabel;
    private int maxRpm;
    private double maxTorque;
    private double maxHorsePower;
    private double maxKiloWatts;

    /**
     * Default constructor for StatsPanel.
     */
    public StatsPanel() {
        maxRpmLabel = new Label("Max RPM: 0");
        maxTorqueLabel = new Label("Max Torque: 0");
        maxHorsePowerLabel = new Label("Max Horsepower: 0");
        maxKiloWattsLabel = new Label("Max Kilowatts: 0");

        this.getChildren().addAll(maxRpmLabel, maxTorqueLabel, maxHorsePowerLabel, maxKiloWattsLabel);
        this.setSpacing(10);
    }

    /**
     * Updates the stats values displayed in the panel. 
     *
     * @param rpm current RPM
     * @param torque current torque
     * @param horsePower current power in  horsepower
     * @param kiloWatts current power in kilowatts
     */
    public void updateStats(final int rpm, final double torque, final double horsePower, final double kiloWatts) {
        Platform.runLater(() -> {
            if (rpm > maxRpm) {
                maxRpm = rpm;
                maxRpmLabel.setText("Max RPM: " + maxRpm);
            }
            if (torque > maxTorque) {
                maxTorque = torque;
                maxTorqueLabel.setText("Max Torque: " + formatDouble(maxTorque));
            }
            if (horsePower > maxHorsePower) {
                maxHorsePower = horsePower;
                maxHorsePowerLabel.setText("Max Horsepower: " + formatDouble(maxHorsePower));
            }
            if (kiloWatts > maxKiloWatts) {
                maxKiloWatts = kiloWatts;
                maxKiloWattsLabel.setText("Max Kilowatts: " + formatDouble(maxKiloWatts));
            }

            this.getChildren().setAll(maxRpmLabel, maxTorqueLabel, maxHorsePowerLabel, maxKiloWattsLabel);
        });
    }

    /**
     * Formats a double value to one decimal place.
     *
     * @param value the double value to format
     * @return the formatted string
     */
    private String formatDouble(final double value) {
        return String.format("%.1f", value);
    }
}

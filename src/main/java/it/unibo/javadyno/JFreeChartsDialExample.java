package it.unibo.javadyno;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.*;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.general.DefaultValueDataset;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JFreeChartsDialExample extends Application {

    private final DefaultValueDataset dataset = new DefaultValueDataset(0);

    @Override
    public void start(Stage stage) {
        DialPlot dialPlot = new DialPlot();
        dialPlot.setDataset(dataset);

        // Cornice e sfondo
        dialPlot.setDialFrame(new StandardDialFrame());
        dialPlot.setBackground(new DialBackground(Color.WHITE));

        // Scala RPM
        StandardDialScale scale = new StandardDialScale(0, 8000, -120, -300, 1000, 4);
        scale.setTickRadius(0.88);
        scale.setTickLabelOffset(0.15);
        scale.setTickLabelFont(new Font("Dialog", Font.BOLD, 14));
        dialPlot.addScale(0, scale);

        // Zona rossa (6000–8000)
        StandardDialRange red = new StandardDialRange(6000, 8000, Color.RED);
        red.setInnerRadius(0.52);
        red.setOuterRadius(0.55);
        dialPlot.addLayer(red);

        // Ago
        DialPointer.Pointer needle = new DialPointer.Pointer();
        dialPlot.addPointer(needle);

        // Creazione grafico
        JFreeChart chart = new JFreeChart(dialPlot);
        chart.setTitle("Contagiri (RPM)");

        // Viewer JavaFX
        ChartViewer viewer = new ChartViewer(chart);
        StackPane root = new StackPane(viewer);
        Scene scene = new Scene(root, 500, 400);

        stage.setTitle("Simulazione Contagiri");
        stage.setScene(scene);
        stage.show();

        // Avvio simulazione fluida
        animateToValueLogarithmic(8000, 15000); // Simula accelerazione a 7000 RPM in 3 secondi
    }

    // Metodo per animare la lancetta con effetto logaritmico fluido
    private void animateToValueLogarithmic(double targetRPM, int durationMillis) {
        double startRPM = dataset.getValue().doubleValue();
        double delta = targetRPM - startRPM;
        long startTime = System.currentTimeMillis();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            long elapsed = System.currentTimeMillis() - startTime;
            double t = Math.min(1.0, (double) elapsed / durationMillis); // normalizzato [0,1]

            double alpha = 5.0; // controlla la curva di easing
            double easedProgress = 1 - Math.exp(-alpha * t); // easing logaritmico
            double currentRPM = startRPM + delta * easedProgress;

            Platform.runLater(() -> dataset.setValue(currentRPM));

            if (t >= 1.0) {
                executor.shutdown();
            }
        }, 0, 16, TimeUnit.MILLISECONDS); // ~60 FPS
    }

    public static void main(String[] args) {
        launch();
    }
}

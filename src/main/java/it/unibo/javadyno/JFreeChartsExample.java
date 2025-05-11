package it.unibo.javadyno;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class JFreeChartsExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX with JFreeChart Dual Y-Axis");
        primaryStage.show();
    }

    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            // Primary dataset
            XYSeries series1 = new XYSeries("Primary Y");
            series1.add(1, 100);
            series1.add(2, 200);
            series1.add(3, 300);
            XYSeriesCollection dataset1 = new XYSeriesCollection(series1);
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Dual Y-Axis Chart",
                    "X",
                    "Primary Y-Axis",
                    dataset1
            );

            XYPlot plot = chart.getXYPlot();

            // Secondary dataset
            XYSeries series2 = new XYSeries("Secondary Y");
            series2.add(1, 5);
            series2.add(2, 15);
            series2.add(3, 25);
            XYSeriesCollection dataset2 = new XYSeriesCollection(series2);

            // Secondary axis
            NumberAxis secondaryAxis = new NumberAxis("Secondary Y-Axis");
            plot.setRangeAxis(1, secondaryAxis);
            plot.setDataset(1, dataset2);
            plot.mapDatasetToRangeAxis(1, 1);

            // Third dataset
            XYSeries series3 = new XYSeries("Third Y");
            series3.add(1, 60);
            series3.add(2, 70);
            series3.add(3, 80);
            XYSeriesCollection dataset3 = new XYSeriesCollection(series3);

            // Third axis
            NumberAxis thirdAxis = new NumberAxis("Third Y-Axis");
            plot.setRangeAxis(2, thirdAxis);
            plot.setDataset(2, dataset3);
            plot.mapDatasetToRangeAxis(2, 2);

            // Renderer for all the axis
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            plot.setRenderer(0, renderer);

            XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
            plot.setRenderer(1, renderer2);

            XYLineAndShapeRenderer renderer3 = new XYLineAndShapeRenderer();
            plot.setRenderer(2, renderer3);

            ChartPanel chartPanel = new ChartPanel(chart);
            swingNode.setContent(chartPanel);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}

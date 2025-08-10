package io.hdmpedro.financeiro.view.components;


import io.hdmpedro.financeiro.util.TemaCores;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Map;

public class ChartPanelModerno extends JPanel {

    public ChartPanelModerno() {
        setLayout(new BorderLayout());
        setBackground(TemaCores.SURFACE);
    }

    public void createPieChart(String title, Map<String, BigDecimal> data) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        data.forEach((key, value) -> dataset.setValue(key, value.doubleValue()));

        JFreeChart chart = ChartFactory.createPieChart(
                title,
                dataset,
                true,
                true,
                false
        );

        customizePieChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(TemaCores.SURFACE);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        removeAll();
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void createBarChart(String title, String xAxisLabel, String yAxisLabel,
                               Map<String, BigDecimal> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.forEach((key, value) -> dataset.addValue(value.doubleValue(), "Valores", key));

        JFreeChart chart = ChartFactory.createBarChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset
        );

        customizeBarChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(TemaCores.SURFACE);
        chartPanel.setPreferredSize(new Dimension(600, 400));

        removeAll();
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void customizePieChart(JFreeChart chart) {
        chart.setBackgroundPaint(TemaCores.SURFACE);
        chart.getTitle().setPaint(TemaCores.TEXT_PRIMARY);
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(TemaCores.SURFACE);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        plot.setLabelPaint(TemaCores.TEXT_PRIMARY);

        Color[] colors = {
                TemaCores.PRIMARY,
                TemaCores.ACCENT,
                TemaCores.SUCCESS,
                TemaCores.ERROR,
                TemaCores.WARNING,
                new Color(156, 39, 176),
                new Color(0, 188, 212),
                new Color(121, 85, 72)
        };

        int colorIndex = 0;
        for (Object key : ((DefaultPieDataset<?>) plot.getDataset()).getKeys()) {
            plot.setSectionPaint((Comparable<?>) key, colors[colorIndex % colors.length]);
            colorIndex++;
        }
    }

    private void customizeBarChart(JFreeChart chart) {
        chart.setBackgroundPaint(TemaCores.SURFACE);
        chart.getTitle().setPaint(TemaCores.TEXT_PRIMARY);
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(TemaCores.SURFACE);
        plot.setRangeGridlinePaint(TemaCores.BORDER);
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, TemaCores.PRIMARY);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
    }
}

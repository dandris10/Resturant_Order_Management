package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class MultiGraphSwingApp {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Graphs with Swing Components");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Panel to hold everything
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Add a label at the top
        JLabel titleLabel = new JLabel("Dashboard with Multiple Graphs", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create a panel for the charts
        JPanel chartsPanel = new JPanel();
        chartsPanel.setLayout(new GridLayout(1, 2, 10, 10)); // 1 row, 2 columns

        // Add two charts
        chartsPanel.add(createChartPanel("Sales Data", "Months", "Sales", getSampleDataset1()));
        chartsPanel.add(createChartPanel("Revenue Data", "Months", "Revenue", getSampleDataset2()));

        mainPanel.add(chartsPanel, BorderLayout.CENTER);

        // Add a panel with a button at the bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Data refreshed!"));
        bottomPanel.add(refreshButton);

        JLabel infoLabel = new JLabel("Last Updated: Just Now");
        bottomPanel.add(infoLabel);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Set up the frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Method to create a chart panel
    private static ChartPanel createChartPanel(String title, String categoryAxisLabel, String valueAxisLabel, DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                title,
                categoryAxisLabel,
                valueAxisLabel,
                dataset
        );
        return new ChartPanel(chart);
    }

    // Sample dataset 1
    private static DefaultCategoryDataset getSampleDataset1() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(120, "Sales", "January");
        dataset.addValue(150, "Sales", "February");
        dataset.addValue(200, "Sales", "March");
        return dataset;
    }

    // Sample dataset 2
    private static DefaultCategoryDataset getSampleDataset2() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100, "Revenue", "January");
        dataset.addValue(140, "Revenue", "February");
        dataset.addValue(180, "Revenue", "March");
        return dataset;
    }
}
